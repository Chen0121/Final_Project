package com.example.chen.final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonPatient_Intake_Form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPatient_Intake_Form = (Button)findViewById(R.id.mainActivity_PatientIntakeForm);
        buttonPatient_Intake_Form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Patient_Intake_form.class);
                startActivityForResult(intent, 50);
            }
        });

    }
}
