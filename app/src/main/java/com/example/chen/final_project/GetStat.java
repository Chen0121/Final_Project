package com.example.chen.final_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import static java.lang.Integer.MAX_VALUE;

public class GetStat extends Activity {

    private TextView l;
    private TextView s;
    private TextView a;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String query;
    private QuizDatabaseHelper dbHelper=new QuizDatabaseHelper(this);
    private int sum;
    private String str_shortest;
    private String str_longest;
    private String str_average;

    String table_name = QuizDatabaseHelper.table_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stat);

        db = dbHelper.getWritableDatabase();
        l=findViewById(R.id.view1);
        s=findViewById(R.id.view2);
        a=findViewById(R.id.view3);

        query="SELECT * FROM " + table_name + ";";
        cursor=db.rawQuery(query,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
            int length=question.length();
            int shortest = 0;
            int longest=0;

            if(length > longest){
                longest = question.length();
                str_longest = question;
            }else if(length < shortest){
                shortest = question.length();
                str_shortest = question;
            }
            sum+=length;
            cursor.moveToNext();
        }

        l.setText("The longest is : "+str_longest);
        s.setText("The shortest is : "+str_shortest);
//        int ave = sum / (count_multichoice);
//        a.setText("average length is " + ave + " characters");
}
}