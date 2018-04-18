package com.example.chen.final_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Integer.MAX_VALUE;

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

        TextView longestQ = findViewById(R.id.view1);
        TextView shortestQ = findViewById(R.id.view2);
        TextView average = findViewById(R.id.view3);

        Bundle bundle = getIntent().getBundleExtra("StatsItem");
        int tot = bundle.getInt("tot");
        int m = bundle.getInt("mc");
        int t = bundle.getInt("tf");
        int n = bundle.getInt("num");

        int lS = bundle.getInt("long");
        String lQ = bundle.getString("longQ");
        int sS = bundle.getInt("short");
        String sQ = bundle.getString("shortQ");
        String avg = bundle.getString("average");

        longestQ.setText( "\""+lQ+"\"");
        shortestQ.setText( "\""+sQ+"\"");
        average.setText(avg);
//        db = dbHelper.getWritableDatabase();
//        TextView tv_long = findViewById(R.id.view1);
//        TextView tv_short = findViewById(R.id.view2);
//        TextView tv_average = findViewById(R.id.view3);
//
//        String long_que= bundle.getString("lQuestion");
//        Log.i("here",long_que);
//        String short_que = bundle.getString("sQuestion");
//        Log.i("here",long_que);
//        int aver= bundle.getInt("average");
//        int shortest=bundle.getInt("short");
//        int longest=bundle.getInt("long");
//
//        tv_long.setText("longest question is :\n" + long_que
//                + ", \n" + longest + " characters");
//        tv_short.setText("shortest question is :\n" + short_que
//                + ", \n" + shortest + " characters");
//        tv_average.setText("average question length is " + aver + " characters");

    }
}