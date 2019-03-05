package databasepractice.sofia.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SofiaDatabaseHelper extends SQLiteOpenHelper {
    //创建表
    //表的名字
    public static final String CREATE_BOOT = "create table Book ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "price real, "
            + "pages integer, "
            + "name text)";

    private static final String CREATE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text, "
            + "category_code integer)";

    private Context mContext;

    public SofiaDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //建Book表语句
        //如果Book表已经存在则不会再次创建
        sqLiteDatabase.execSQL(CREATE_BOOT);

        //创建Table表用于管理图书分类
        sqLiteDatabase.execSQL(CREATE_CATEGORY);
        //跨进程访问不能使用Toast,通过内容提供器加入外部访问的接口

//        Toast.makeText(mContext,"Create successed", Toast.LENGTH_SHORT).show();
    }

    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //如果Book表存在则删除,这会有问题，每次升级数据库都会要把所有的数据删除掉
        sqLiteDatabase.execSQL("drop table if exists Book");
        sqLiteDatabase.execSQL("drop table if exists Category");
        onCreate(sqLiteDatabase);
    }
}
