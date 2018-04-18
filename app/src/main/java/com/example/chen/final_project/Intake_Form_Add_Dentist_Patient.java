package com.example.chen.final_project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Intake_Form_Add_Dentist_Patient extends Activity {

    static final String ACTIVITY_NAME = "Intake_Form_Add_DENTIST_Patient";

    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;

    EditText intakeForm_AddPatientName_Dentist;
    EditText intakeForm_AddPatientAddress_Dentist;
    EditText intakeForm_AddPatientBirthday_Dentist;
    EditText intakeForm_AddPatientPhone_Dentist;
    EditText intakeForm_AddPatientHealthcard_Dentist;
    EditText intakeForm_AddPatientDescription_Dentist;
    EditText intakeForm_AddPatientBenefit_Dentist;
    CheckBox intakeForm_AddPatientHadbraces_Dentist;

    Button buttonSumbit;
    Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_add_dentist_patient);

        intakeForm_AddPatientName_Dentist = (EditText)findViewById(R.id.intakeForm_AddPatientName_Dentist);
        intakeForm_AddPatientAddress_Dentist = (EditText)findViewById(R.id.intakeForm_AddPatientAddress_Dentist);
        intakeForm_AddPatientBirthday_Dentist = (EditText)findViewById(R.id.intakeForm_AddPatientBirthday_Dentist);
        intakeForm_AddPatientPhone_Dentist =(EditText)findViewById(R.id.intakeForm_AddPatientPhone_Dentist);
        intakeForm_AddPatientHealthcard_Dentist = (EditText)findViewById(R.id.intakeForm_AddPatientHealthcard_Dentist);
        intakeForm_AddPatientDescription_Dentist =(EditText)findViewById(R.id.intakeForm_AddPatientDescription_Dentist);
        intakeForm_AddPatientBenefit_Dentist =(EditText)findViewById(R.id.intakeForm_AddPatientBenifits_Dentist);
        intakeForm_AddPatientHadbraces_Dentist = (CheckBox) findViewById(R.id.intakeForm_AddPatientHadbraces_Dentist);

        buttonSumbit = (Button)findViewById(R.id.intakeForm_AddPatientSumbitButton_Dentist);

        //Connecting the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(this);
        db = patientDatabaseHelper.getWritableDatabase();

        buttonSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get all inputs values.
                ContentValues newData = new ContentValues();

                newData.put(PatientDatabaseHelper.COLUMN_NAME,intakeForm_AddPatientName_Dentist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_ADDRESS,intakeForm_AddPatientAddress_Dentist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_BIRTHDAY, intakeForm_AddPatientBirthday_Dentist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_PHONE, intakeForm_AddPatientPhone_Dentist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_HEALTHCARD, intakeForm_AddPatientHealthcard_Dentist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_DESCRIPTION,intakeForm_AddPatientDescription_Dentist.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_BENEFITS,intakeForm_AddPatientBenefit_Dentist.getText().toString());
                if(intakeForm_AddPatientHadbraces_Dentist.isChecked()) {
                    newData.put(PatientDatabaseHelper.COLUMN_HADBRACES, intakeForm_AddPatientHadbraces_Dentist.isChecked());
                }

                //Saving input data to database
                db.insert(PatientDatabaseHelper.TABLE_NAME,null, newData);

                intakeForm_AddPatientName_Dentist.setText("");
                intakeForm_AddPatientAddress_Dentist.setText("");
                intakeForm_AddPatientBirthday_Dentist.setText("");
                intakeForm_AddPatientPhone_Dentist.setText("");
                intakeForm_AddPatientHealthcard_Dentist.setText("");
                intakeForm_AddPatientDescription_Dentist.setText("");
                intakeForm_AddPatientBenefit_Dentist.setText("");

                Intent returnIntent = new Intent(Intake_Form_Add_Dentist_Patient.this, Patient_Intake_form.class);
                startActivity(returnIntent);
                finish();

            }
        });

        buttonCancel=(Button)findViewById(R.id.intakeForm_AddPatientCancelButton_Dentist);
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
