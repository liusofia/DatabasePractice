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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //版本号可以让onUpdate方法执行起来
        //数据库的名字
        dbHelper = new SofiaDatabaseHelper(this,"BookStore.db",null,2);
        Button createDatabase = (Button)findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }
}
