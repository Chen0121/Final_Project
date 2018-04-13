package com.example.chen.final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chen.finalproject.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        switch (id) {
            case R.id.option1:
                Intent Quiz=new Intent(MainActivity.this,Quiz_Creater.class);
                startActivity(Quiz);
                break;
            case R.id.option2:


                break;
            case R.id.option3:


                break;
            case R.id.option4:


                break;
            case R.id.about:
                CharSequence text = "Version 1.0 by Chen Huang, Zeyang Hu, Lei Cao, Junjin Chen ";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this , text, duration);
                toast.show();
                break;
        }
        return true;
    }
}
