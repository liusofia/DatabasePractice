package databasepractice.sofia.com.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

//这个是内容提供器，任何一个应用程序都可以使用ContentResolver来访问我们程序中的数据
//所有的CRUD操作都一定要匹配到相应的内容URI格式才能进行的
public class DatabaseProvider extends ContentProvider {
    //表示访问table1中的所有数据
    public static final int BOOK_DIR = 0;
    //表示访问table1中的单条数据
    public static final int BOOK_ITEM = 1;
    //表示访问table2表中的所有数据
    public static final int CATEGORY_DIR = 2;
    //表示访问table2表中的单条数据
    public static final int CATEGORY_ITEM = 3;
    public static final String AUTHORITY = "databasepractice.sofia.com.database";
    private static UriMatcher uriMatcher;
    private SofiaDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //三个参数：AUTHORITY,PATH,自定义代码
        //将期望匹配的内容uri格式传递进去，这个路径可以使用通配符
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }

    public DatabaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRow = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Book","id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRow = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRow = db.delete("Category","id = ?",new String[]{categoryId});
                break;
                default:
                    break;
        }
        return  deleteRow;
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.databasepractice.sofia.com.database.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.databasepractice.sofia.com.database.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.databasepractice.sofia.com.database.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.databasepractice.sofia.com.database.category";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //getReadableDatabase()和getWritableDatabase();这两个方法作用:创建或打开一个现有的数据库(如果数据库已存在则直接打开，否则创建一个新的数据库)并返回一个可对数据进行读写操作的对象;不同的是当数据库不可写入的时候(如磁盘已满),getReadableDatabase()方法返回的对象将以只读的方式去打开数据库，而getWriteableDatabase()方法出现异常
        //借助getReadableDatabase和getWritableDatabase方法的返回值用于进行数据库操作
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book",null,values);
                //Uri.parse()将内容uri解析成Uri对象；
                //这个内容Uri是以新增数据的id结尾的
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category",null,values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                default:
                    break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SofiaDatabaseHelper(getContext(),"BookStroe.db",null,2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            //***************************************************************
            //匹配成功返回相应自定义代码，我们就可以判断出调用方期望访问的到底是什么数据了
            case BOOK_DIR:
                //查询table1表中的所有数据
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                //查询table1表中的单条数据
                //getPathSegments会将内容Uri权限之后的部分以“/”符号进行分割，并把分割后的结果放在一个字符串列表中，这个列表的第0
                //位存放的就是路径，这个字符串列表的第一个位置防止的就是id了
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                //查询table2表中的所有数据
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                //查询table2表中的单条数据
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",projection,"id = ?",new String[]{categoryId},null,null,sortOrder);
                break;
                default:
                    break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book",values,"id = ?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category",values,"id = ?",new String[]{categoryId});
                break;
                default:
                    break;
        }
        return updateRows;
    }
}
