package databasepractice.sofia.com.database;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private SofiaDatabaseHelper dbHelper;
    private Button mAddData;
    private Button mUpdateData;
    private Button mRemoveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //版本号可以让onUpdate方法执行起来
        //数据库的名字
        dbHelper = new SofiaDatabaseHelper(this,"BookStore.db",null,4);
        Button createDatabase = (Button)findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建数据库
                dbHelper.getWritableDatabase();
            }
        });

        //添加数据
        mAddData = (Button) findViewById(R.id.add_data);
        mAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name","hahaji");
                values.put("author","miao haha");
                values.put("price","222");
                values.put("pages","800");
                db.insert("Book",null,values);
                values.clear();

                values.put("name","hahaji2");
                values.put("author","miao haha2");
                values.put("pages","810");
                values.put("price","900");
                db.insert("Book",null,values);
            }
        });

        //更新数据
        mUpdateData = (Button)findViewById(R.id.update_data);
        mUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",11.11);
                //更新哪个表中的哪个值，哪一列
                db.update("Book",values,"name = ?",new String[]{"hahaji"});
            }
        });

        //删除数据
        mRemoveData = (Button)findViewById(R.id.remove_data);
        mRemoveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book","pages > ?" ,new String[]{"805"});
            }
        });
    }
}
