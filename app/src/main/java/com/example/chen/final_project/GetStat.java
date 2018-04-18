package com.example.chen.final_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GetStat extends Activity {

    private SQLiteDatabase db=null;
    private Cursor cursor;
    private String query;
    private QuizDatabaseHelper dbHelper=new QuizDatabaseHelper(this);
    private String str_average;
    private String table_name = QuizDatabaseHelper.table_name;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stat);

        TextView tv_long = findViewById(R.id.view1);
        TextView tv_short = findViewById(R.id.view2);
        TextView tv_average = findViewById(R.id.view3);
        bundle = getIntent().getBundleExtra("stats");

        int sum = bundle.getInt("sum");

        String long_que= bundle.getString("lQuestion");
        String short_que = bundle.getString("sQuestion");
        String aver= new Integer(bundle.getInt("average")).toString();

        tv_long.setText(long_que);
        tv_short.setText(short_que);
        tv_average.setText(aver);

    }
}