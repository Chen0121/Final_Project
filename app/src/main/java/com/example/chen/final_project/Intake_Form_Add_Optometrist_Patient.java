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

public class Intake_Form_Add_Optometrist_Patient extends Activity {

    static final String ACTIVITY_NAME = "Intake_Form_Add_Optometrist_Patient";
    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;

    EditText intakeForm_AddPatientName_Optometrist;
    EditText intakeForm_AddPatientAddress_Optometrist;
    EditText intakeForm_AddPatientBirthday_Optometrist;
    EditText intakeForm_AddPatientPhone_Optometrist;
    EditText intakeForm_AddPatientHealthcard_Optometrist;
    EditText intakeForm_AddPatientDescription_Optometrist;
    EditText intakeForm_AddPatientGlassesbought_Optometrist;
    EditText intakeForm_AddPatientGlassesstore_Optometrist;

    Button buttonSumbit;
    Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_add_optometrist_patient);

        intakeForm_AddPatientName_Optometrist = (EditText)findViewById(R.id.intakeForm_AddPatientName_Optometrist);
        intakeForm_AddPatientAddress_Optometrist = (EditText)findViewById(R.id.intakeForm_AddPatientAddress_Optometrist);
        intakeForm_AddPatientBirthday_Optometrist = (EditText)findViewById(R.id.intakeForm_AddPatientBirthday_Optometrist);
        intakeForm_AddPatientPhone_Optometrist =(EditText)findViewById(R.id.intakeForm_AddPatientPhone_Optometrist);
        intakeForm_AddPatientHealthcard_Optometrist = (EditText)findViewById(R.id.intakeForm_AddPatientHealthcard_Optometrist);
        intakeForm_AddPatientDescription_Optometrist =(EditText)findViewById(R.id.intakeForm_AddPatientDescription_Optometrist);
        intakeForm_AddPatientGlassesbought_Optometrist =(EditText)findViewById(R.id.intakeForm_AddPatientGlassesbought_Optometrist);
        intakeForm_AddPatientGlassesstore_Optometrist = (EditText)findViewById(R.id.intakeForm_AddPatientGlassesstore_Optometrist);

        buttonSumbit = (Button)findViewById(R.id.intakeForm_AddPatientSumbitButton_Optometrist);
        //Connecting the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(this);
        db = patientDatabaseHelper.getWritableDatabase();

        //click the submit button to send inputs to database
        buttonSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all inputs values.
                ContentValues newData = new ContentValues();

                newData.put(PatientDatabaseHelper.COLUMN_NAME,intakeForm_AddPatientName_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_ADDRESS,intakeForm_AddPatientAddress_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_BIRTHDAY, intakeForm_AddPatientBirthday_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_PHONE, intakeForm_AddPatientPhone_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_HEALTHCARD, intakeForm_AddPatientHealthcard_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_DESCRIPTION,intakeForm_AddPatientDescription_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_GLASSESBOUGHT, intakeForm_AddPatientGlassesbought_Optometrist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_GLASSESSTORE,intakeForm_AddPatientGlassesstore_Optometrist.getText().toString());

                //Saving input data to database
                db.insert(PatientDatabaseHelper.TABLE_NAME,null, newData);

                intakeForm_AddPatientName_Optometrist.setText("");
                intakeForm_AddPatientAddress_Optometrist.setText("");
                intakeForm_AddPatientBirthday_Optometrist.setText("");
                intakeForm_AddPatientPhone_Optometrist.setText("");
                intakeForm_AddPatientHealthcard_Optometrist.setText("");
                intakeForm_AddPatientDescription_Optometrist.setText("");
                intakeForm_AddPatientGlassesbought_Optometrist.setText("");
                intakeForm_AddPatientGlassesstore_Optometrist.setText("");

                Intent returnIntent = new Intent(Intake_Form_Add_Optometrist_Patient.this, Patient_Intake_form.class);
                startActivity(returnIntent);
                finish();
            }
        });

        buttonCancel=(Button)findViewById(R.id.intakeForm_AddPatientCancelButton_Optometrist);
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
