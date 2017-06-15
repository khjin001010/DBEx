package khjin001010.kr.hs.emirim.dbex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button butInit;
    Button but_insert, but_update, but_delete;
    Button but_select;
    EditText edit_group_name;
    EditText edit_group_count;
    EditText edit_result_name;
    EditText edit_result_count;
    MyDBHelper myDb;
    SQLiteDatabase sqliteDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        butInit = (Button)findViewById(R.id.but_init);
        but_insert = (Button)findViewById(R.id.but_insert);
        but_select = (Button)findViewById(R.id.but_select);
        but_update = (Button)findViewById(R.id.but_update);
        edit_group_name = (EditText)findViewById(R.id.edit_group_name);
        edit_group_count = (EditText)findViewById(R.id.edit_group_count);
        edit_result_name = (EditText)findViewById(R.id.edit_result_name);
        edit_result_count = (EditText)findViewById(R.id.edit_result_cout);
        but_delete = (Button)findViewById(R.id.but_delete);

        //DB생성
        myDb = new MyDBHelper(this); // == myHelper 라고 쓰는 것도 좋다(변수명)
        //기조의 테이블이 존재하면 삭제하고 테이블을 생성한다.

        butInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDb = myDb.getWritableDatabase(); //SQLiteDatabase 참조 값
                myDb.onUpgrade(sqliteDb,1,2);
                sqliteDb.close();
            }
        });

        but_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDb = myDb.getWritableDatabase(); //insert문을 실행해야 하니까 writable
                String sql = "insert into idoltable values('"+edit_group_name.getText()+"', "+edit_group_count.getText()+")";
                sqliteDb.execSQL(sql);
                sqliteDb.close();
                Toast.makeText(MainActivity.this, "저장이 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        but_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDb = myDb.getReadableDatabase(); //읽기 가능한 데이터베이스 | 변경 가능 - write , 조회만 - read
                String sql = "select * from idoltable";
                String names = "Idol 이름"+"\r\n"+"==================="+"\r\n"; //\r : 커서를 처음으로 옮겨줌
                String counts = "Idol 인원수"+"\r\n"+"==================="+"\r\n";
                Cursor cursor = sqliteDb.rawQuery(sql, null); //결과행에 있는 데이터들을 선택해서 커서를 반환
                while(cursor.moveToNext()){ //next할 데이터행이 있는 동안 반복
                    names += cursor.getString(0)+"\r\n";
                    counts += cursor.getInt(1)+"\r\n";
                }
                edit_result_name.setText(names);
                edit_result_count.setText(counts);
                sqliteDb.close();
            }
        });

        but_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "update idoltable set idolCount="+edit_group_count.getText()+" where idolName='"+edit_group_name.getText()+"'";
                String sql2 = "select * from idoltable";
                sqliteDb = myDb.getReadableDatabase();
                sqliteDb.execSQL(sql);
                sqliteDb.close();
                Toast.makeText(MainActivity.this, "수정이 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        but_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDb = myDb.getWritableDatabase();
                String sql = "delete from idoltable where idolName = '"+edit_group_name.getText()+"'";
                sqliteDb.execSQL(sql);
                sqliteDb.close();
                Toast.makeText(MainActivity.this, "삭제가 성공적으로 완료되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

    }

    // 내부 클래스 생성
    class MyDBHelper extends SQLiteOpenHelper{ //SQLiteOpenHelper : 추상클래스
        // 생성자 생성
        /*public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }*/
        // 직접 대입
        public MyDBHelper(Context context) { //idolDB라는 이름의 데이터베이스가 생성된다.
            super(context, "idolDB", null, 1); // idolDB는 우리가 만들어 놓은 form에 딱 한번만 생성이 된다.
            // 만약, 새롭게 시작하고 싶다면 버전의 숫자를 올려주면 된다!
            // 한 번 만들어 진 것은 바꿔쓰기가 가능하지 않는다.
        }

        // idolTabele라는 이름의 테이블 생성
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table idoltable(idolName text not null primary key, idolCount Integer)";
            db.execSQL(sql);
        }
        // 이미 idolTable이 존재한다면 기존의 테이블을 삭제하고, 새로 테이블 만들 때 호출
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // idol table이 존재하는데 초기화 버튼 클릭 시 실행됨
            String sql = "drop table if exists idoltable"; //만약 idoltable이 존재한다면 table을 삭제해라.
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
