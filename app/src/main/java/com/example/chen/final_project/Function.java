package com.example.chen.final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Function extends AppCompatActivity {

    Button btn_import;
    Button btn_create;
    Button btn_stat;
    Button btn_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple__choice__quiz__creater);
        btn_import=findViewById(R.id.btn_import);
        btn_create=findViewById(R.id.btn_create);
        btn_stat=findViewById(R.id.btn_stat);
        btn_quit=findViewById(R.id.btn_quit);

//        btn_import.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
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
