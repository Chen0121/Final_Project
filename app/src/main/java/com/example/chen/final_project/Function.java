package com.example.chen.final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;



public class Function extends AppCompatActivity {

    String parse_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_quiz_creater);

        Button btn_import = (Button)findViewById(R.id.btn_import);
        Button btn_create = (Button)findViewById(R.id.btn_create);
        Button btn_stat   = (Button)findViewById(R.id.btn_stat);
        Button btn_quit   = (Button)findViewById(R.id.btn_quit);

//        btn_import.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateQuiz.QuizQuery query=new QuizQuery();
//                query.execute();
//            }
//        });

        btn_create.setOnClickListener(v -> {
            Intent intent_c=new Intent(Function.this,CreateQuiz.class);
            startActivity(intent_c);
        });

        btn_stat.setOnClickListener(v -> {
            Intent intent_s=new Intent(Function.this,GetStat.class);
            startActivity(intent_s);
        });

        btn_quit.setOnClickListener(v -> {
            Intent intent_q=new Intent(Function.this,MainActivity.class);
            startActivity(intent_q);
        });
    }
}
