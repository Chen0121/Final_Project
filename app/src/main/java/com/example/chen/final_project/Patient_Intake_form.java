package com.example.chen.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Patient_Intake_form extends AppCompatActivity {

    Button buttonRegisterDoctor;
    Button buttonRegisterDentist;
    Button buttonRegisterOptometrist;
    Button buttonCalculatePatientAge;

    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_form);

        //Use the Singleton to connect to the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(this);
        db = patientDatabaseHelper.getWritableDatabase();



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

        buttonCalculatePatientAge = (Button)findViewById(R.id.buttonCalculatePatientAge);
        buttonCalculatePatientAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = db.rawQuery("SELECT "+ PatientDatabaseHelper.COLUMN_BIRTHDAY + " FROM " + PatientDatabaseHelper.TABLE_NAME,null);
                cursor.moveToFirst();
                ArrayList<Integer> arraylist= new ArrayList<>();
                int total=0;
                int averageDOB=0;
                int patientAveAge;
                int patientMax;
                int patientMaxAge;
                int patientMin;
                int patientMinAge;

                //Calculate the the Average Age for all patients
                for(int i=0; i<cursor.getCount(); i++){
                    int p = Integer.parseInt(cursor.getString(cursor.getColumnIndex("COL_BIRTHDAY")).substring(0,4));
                    arraylist.add(p);
                    cursor.moveToNext();
                }
                for(int j =0; j < arraylist.size();j++){
                    total +=arraylist.get(j);
                }
                averageDOB = total/arraylist.size();
                patientAveAge = 2018 -averageDOB;

                //Calculate the youngest Age for all patients
                patientMax = arraylist.get(0);
                for(int k =0; k< arraylist.size(); k++){
                    if(arraylist.get(k)>patientMax){
                        patientMax = arraylist.get(k);
                    }
                }
                patientMinAge = 2018 - patientMax;

                //Calculate the eldest Age for all patients
                patientMin = arraylist.get(0);
                for(int m = 0; m<arraylist.size();m++){
                    if(arraylist.get(m)<patientMin){
                        patientMin = arraylist.get(0);
                    }
                }
                patientMaxAge = 2018 - patientMin;

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(Patient_Intake_form.this);
                alertdialog.setTitle("The ages for all patient are ");
                alertdialog.setMessage("Average age : "+String.valueOf(patientAveAge)+"\n"+"Eldest age : "+String.valueOf(patientMaxAge)+"\n"+"Youngest age : " +String.valueOf(patientMinAge));

                alertdialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                AlertDialog dialogshow = alertdialog.create();
                dialogshow.show();
            }
        });
        
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
