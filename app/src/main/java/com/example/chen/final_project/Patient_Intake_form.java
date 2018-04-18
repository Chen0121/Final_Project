package com.example.chen.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Patient_Intake_form extends AppCompatActivity {

    Button buttonRegisterDoctor;
    Button buttonRegisterDentist;
    Button buttonRegisterOptometrist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_form);

        buttonRegisterDoctor = (Button)findViewById(R.id.buttonRegisterDoctor);
        buttonRegisterDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Intake_form.this, IntakeFormDoctorListView.class);
                startActivityForResult(intent,40);
            }
        });

        buttonRegisterDentist = (Button)findViewById(R.id.buttonRegisterDentist);
        buttonRegisterDentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Patient_Intake_form.this,IntakeFormDentistListView.class);
                startActivity(intent1);
            }
        });

        buttonRegisterOptometrist = (Button)findViewById(R.id.buttonRegisterOptometrist);
        buttonRegisterOptometrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Patient_Intake_form.this,IntakeFormOptometristListView.class);
                startActivity(intent2);
            }
        });
;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.intakeform_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem mi){
        switch (mi.getItemId()){
            case R.id.intakeForm_menuItemMovieInformation:
                final Intent intent1 = new Intent(Patient_Intake_form.this, movie_information.class);
                startActivityForResult(intent1,10);
                break;

            case R.id.intakeForm_menuItemQuizCreater:
                Intent intent2 = new Intent(Patient_Intake_form.this,multiple_choice_quiz_creater.class);
                startActivityForResult(intent2,20);
                break;

            case R.id.intakeForm_menuItemBusRouteApp:
                Intent intent3 = new Intent(Patient_Intake_form.this, octranspo_bus_route_app.class);
                startActivityForResult(intent3, 30);
                break;

            case R.id.aboutZeyangHu:
                Toast toast = Toast.makeText(this,"This Intake Form is made By Zeyang Hu", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.intakeForm_menuItemPatientList:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you wish to Retrieve the Patient List? ");
                String[] patientType ={"Doctor","Dentist","Optometrist"};

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Patient_Intake_form.this, intake_form_patient_list_retrival.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });


                AlertDialog dialog1 = builder.create();
                dialog1.show();
                break;
        }
        return true;
    }
}
