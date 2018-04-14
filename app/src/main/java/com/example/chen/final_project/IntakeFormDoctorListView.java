package com.example.chen.final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IntakeFormDoctorListView extends Activity {

    static final String ACTIVITY_NAME = "DoctorListView";

    Button buttonAddPatient;
    ListView listViewPatientList;
    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayList<Intake_Form_Patient_Info> patientInfoArraylist;
    PatientListAdapter patientListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_doctor_list_view);



        //Use the Singleton to connect to the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(this);
        db = patientDatabaseHelper.getWritableDatabase();

        // scan the entire table and put entry into arraylist patientinfo
        patientInfoArraylist = new ArrayList<>(40);
        cursor =db.query(false,
                    PatientDatabaseHelper.TABLE_NAME,
                    new String[]{PatientDatabaseHelper.COLUMN_NAME, PatientDatabaseHelper.COLUMN_ADDRESS},
                    null,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String patientName = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_NAME));
            String patientAddress = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ADDRESS));
            Intake_Form_Patient_Info object_patient_info = new Intake_Form_Patient_Info(patientName,patientAddress);

            patientInfoArraylist.add(object_patient_info);// add information to arraylist element.

            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_NAME))+
                    cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ADDRESS)));
            cursor.moveToNext();
        }

        //get the List and use Adapter to connect patientInfoArrayList to this listview
        listViewPatientList =(ListView)findViewById(R.id.IntakeForm_PatientListView_Doctor);
        patientListAdapter = new PatientListAdapter(this);
        listViewPatientList.setAdapter(patientListAdapter);

        // add a new Patient
        buttonAddPatient = (Button)findViewById(R.id.buttonAddPatient_Doctor);
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntakeFormDoctorListView.this, Intake_Form_Add_Doctor_Patient.class);
                startActivityForResult(intent, 500);
            }
        });

    }


    private class PatientListAdapter extends ArrayAdapter<String>{

        public PatientListAdapter(@NonNull Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup Parent){
            LayoutInflater inflater = IntakeFormDoctorListView.this.getLayoutInflater();

            View result = null;
            result = inflater.inflate(R.layout.intake_form_patient_listview_layout,null);

            Intake_Form_Patient_Info pi = patientInfoArraylist.get(position);
            TextView patientName = (TextView)result.findViewById(R.id.intakeForm_ListView_Layout_PatientName);
            TextView patientAddress = (TextView)result.findViewById(R.id.intakeForm_ListView_Layout_PatientAddress);
            patientName.setText(pi.getName());
            patientAddress.setText(pi.getAddress());

            return result;
        }

        public int getCount(){
            return patientInfoArraylist.size();
        }

        @Override
        public String getItem(int position){
            return null;
        }

        public long getId(int position){
            return position;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        patientListAdapter = new PatientListAdapter(this);
        listViewPatientList.setAdapter(patientListAdapter);
        listViewPatientList.invalidate();
    }

    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        cursor.close();
        db.close();
    }




}
