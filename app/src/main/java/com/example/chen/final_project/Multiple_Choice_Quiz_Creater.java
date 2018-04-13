package com.example.chen.final_project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chen.final_project.R;

public class Quiz_Creator extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple__choice__quiz__creater);

        Button btn_import=(Button)findViewById(R.id.btn_import);
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent import_page=new Intent(Quiz_Creator.this,ImportQuiz.class);
                startActivity(import_page);
            }
        });

        Button btn_create=(Button)findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create_quiz=new Intent(Quiz_Creator.this,CreateQuiz.class);
                startActivity(create_quiz);
            }
        });

        Button btn_statistic=(Button)findViewById(R.id.btn_statistic);
        btn_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent get_stat=new Intent(Quiz_Creator.this,GetStat.class);
                startActivity(get_stat);
            }
        });

        Button btn_quit=(Button)findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(Quiz_Creator.this,MainActivity.class);
            }
        });

    }
}