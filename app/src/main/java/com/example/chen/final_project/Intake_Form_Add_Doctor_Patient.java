package com.example.chen.final_project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Intake_Form_Add_Doctor_Patient extends Activity {

    static final String ACTIVITY_NAME = "Intake_Form_Add_Doctor_Patient";

    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;


    EditText intakeForm_AddPatientName_Doctor;
    EditText intakeForm_AddPatientAddress_Doctor;
    EditText intakeForm_AddPatientBirthday_Doctor;
    EditText intakeForm_AddPatientPhone_Doctor;
    EditText intakeForm_AddPatientHealthcard_Doctor;
    EditText intakeForm_AddPatientDescription_Doctor;
    EditText intakeForm_AddPatientPreviousSurgery_Doctor;
    EditText intakeForm_AddPatientAllergies_Doctor;

    Button buttonSumbit;
    Button buttonCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_add_doctor_patient);

        intakeForm_AddPatientName_Doctor = (EditText)findViewById(R.id.intakeForm_AddPatientName_Doctor);
        intakeForm_AddPatientAddress_Doctor = (EditText)findViewById(R.id.intakeForm_AddPatientAddress_Doctor);
        intakeForm_AddPatientBirthday_Doctor = (EditText)findViewById(R.id.intakeForm_AddPatientBirthday_Doctor);
        intakeForm_AddPatientPhone_Doctor =(EditText)findViewById(R.id.intakeForm_AddPatientPhone_Doctor);
        intakeForm_AddPatientHealthcard_Doctor = (EditText)findViewById(R.id.intakeForm_AddPatientHealthcard_Doctor);
        intakeForm_AddPatientDescription_Doctor =(EditText)findViewById(R.id.intakeForm_AddPatientDescription_Doctor);
        intakeForm_AddPatientPreviousSurgery_Doctor =(EditText)findViewById(R.id.intakeForm_AddPatientPreviousSurgery_Doctor);
        intakeForm_AddPatientAllergies_Doctor = (EditText)findViewById(R.id.intakeForm_AddPatientAllergies_Doctor);

        buttonSumbit = (Button)findViewById(R.id.intakeForm_AddPatientSumbitButton_Doctor);


        //Connecting the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(this);
        db = patientDatabaseHelper.getWritableDatabase();

        //click the submit button to send inputs to database
        buttonSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all inputs values.
                ContentValues newData = new ContentValues();

                newData.put(PatientDatabaseHelper.COLUMN_NAME,intakeForm_AddPatientName_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_ADDRESS,intakeForm_AddPatientAddress_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_BIRTHDAY, intakeForm_AddPatientBirthday_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_PHONE, intakeForm_AddPatientPhone_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_HEALTHCARD, intakeForm_AddPatientHealthcard_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_DESCRIPTION,intakeForm_AddPatientDescription_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_PREVIOUSSURGERY, intakeForm_AddPatientPreviousSurgery_Doctor.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_ALLERGIES,intakeForm_AddPatientAllergies_Doctor.getText().toString());

                //Saving input data to database
                db.insert(PatientDatabaseHelper.TABLE_NAME,null, newData);

                intakeForm_AddPatientName_Doctor.setText("");
                intakeForm_AddPatientAddress_Doctor.setText("");
                intakeForm_AddPatientBirthday_Doctor.setText("");
                intakeForm_AddPatientPhone_Doctor.setText("");
                intakeForm_AddPatientHealthcard_Doctor.setText("");
                intakeForm_AddPatientDescription_Doctor.setText("");
                intakeForm_AddPatientPreviousSurgery_Doctor.setText("");
                intakeForm_AddPatientAllergies_Doctor.setText("");

                Intent returnIntent = new Intent(Intake_Form_Add_Doctor_Patient.this, Patient_Intake_form.class);
                startActivity(returnIntent);
                finish();
            }
        });

        buttonCancel=(Button)findViewById(R.id.intakeForm_AddPatientCancelButton_Doctor);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
        });


    }


    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


}
