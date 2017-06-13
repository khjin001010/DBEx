package khjin001010.kr.hs.emirim.dbex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            String sql = "drop table if exist idoltable"; //만약 idoltable이 존재한다면 table을 삭제해라.
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
