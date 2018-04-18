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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class IntakeFormDentistListView extends Activity {

    static final String ACTIVITY_NAME = "DentistListView";

    Button buttonAddPatient;
    ListView listViewPatientList;
    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayList<Intake_Form_Patient_Info> patientInfoArraylist = null;
    PatientListAdapter patientListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_dentist_list_view);

        //Use the Singleton to connect to the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(this);
        db = patientDatabaseHelper.getWritableDatabase();

        patientInfoArraylist = new ArrayList<>(40);
        cursor = db.query(false,
                PatientDatabaseHelper.TABLE_NAME,
                new String[]{PatientDatabaseHelper.COLUMN_NAME, PatientDatabaseHelper.COLUMN_ADDRESS,
                        PatientDatabaseHelper.COLUMN_BIRTHDAY, PatientDatabaseHelper.COLUMN_PHONE,
                        PatientDatabaseHelper.COLUMN_HEALTHCARD, PatientDatabaseHelper.COLUMN_DESCRIPTION,
                        PatientDatabaseHelper.COLUMN_PREVIOUSSURGERY, PatientDatabaseHelper.COLUMN_ALLERGIES,
                        PatientDatabaseHelper.COLUMN_GLASSESBOUGHT, PatientDatabaseHelper.COLUMN_GLASSESSTORE,
                        PatientDatabaseHelper.COLUMN_BENEFITS, PatientDatabaseHelper.COLUMN_HADBRACES},
                "COL_PREVIOUSSURGERY IS NULL AND " +"COL_GLASSESBOUGHT IS  NULL ",
                new String[]{}, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String patientName = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_NAME));
            String patientAddress = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ADDRESS));

            Intake_Form_Patient_Info object_patient_info = new Intake_Form_Patient_Info(patientName, patientAddress);

            patientInfoArraylist.add(object_patient_info);// add information to arraylist element.

            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_NAME)) +
                    cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ADDRESS)));
            cursor.moveToNext();
        }
        //get the List and use Adapter to connect patientInfoArrayList to this listview
        listViewPatientList = (ListView) findViewById(R.id.IntakeForm_PatientListView_Dentist);
        patientListAdapter = new PatientListAdapter(this);
        listViewPatientList.setAdapter(patientListAdapter);
        listViewPatientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dbid = patientListAdapter.getItemID(position);
                int listViewPosition = patientListAdapter.getId(position);

                cursor = db.rawQuery("SELECT * FROM " + PatientDatabaseHelper.TABLE_NAME + " WHERE " + PatientDatabaseHelper.COLUMN_ID + " = " + dbid, null);
                cursor.moveToFirst();

                String patientID = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ID));
                String patientName = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_NAME));
                String patientAddress = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ADDRESS));
                String patientBirthDay = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_BIRTHDAY));
                String patientPhone = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_PHONE));
                String patientHealthcard = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_HEALTHCARD));
                String patientDescription = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_DESCRIPTION));
                String patientBenefit = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_BENEFITS));
                String patientHadbraces = cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_HADBRACES));

                Bundle patientBundle = new Bundle();
                int flag = 2;
                patientBundle.putInt("Patient_Flag",flag);
                patientBundle.putInt("Patient_Listview_Position", listViewPosition);
                patientBundle.putString("Patient_ID", patientID);
                patientBundle.putString("Patient_Name", patientName);
                patientBundle.putString("Patient_Address", patientAddress);
                patientBundle.putString("Patient_Birthday", patientBirthDay);
                patientBundle.putString("Patient_Phone", patientPhone);
                patientBundle.putString("Patient_Healthcard", patientHealthcard);
                patientBundle.putString("Patient_Description", patientDescription);
                patientBundle.putString("Patient_SP1", patientBenefit);
                patientBundle.putString("Patient_SP2", patientHadbraces);

                Intent intent = new Intent(IntakeFormDentistListView.this, Intake_Form_Empty_Detail.class);
                intent.putExtra("PatientInfo", patientBundle);
                startActivityForResult(intent, 13, patientBundle);
            }
        });

        // add a new Patient
        buttonAddPatient = (Button) findViewById(R.id.buttonAddPatient_Dentist);
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntakeFormDentistListView.this, Intake_Form_Add_Dentist_Patient.class);
                startActivityForResult(intent, 500);
            }
        });
    }

    private class PatientListAdapter extends ArrayAdapter<String> {

        public PatientListAdapter(@NonNull Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup Parent) {
            LayoutInflater inflater = IntakeFormDentistListView.this.getLayoutInflater();

            View result = null;
            result = inflater.inflate(R.layout.intake_form_patient_listview_layout, null);

            Intake_Form_Patient_Info pi = patientInfoArraylist.get(position);
            TextView patientName = (TextView) result.findViewById(R.id.intakeForm_ListView_Layout_PatientName);
            TextView patientAddress = (TextView) result.findViewById(R.id.intakeForm_ListView_Layout_PatientAddress);
            patientName.setText(pi.getName());
            patientAddress.setText(pi.getAddress());

            return result;
        }

        public int getCount() {
            return patientInfoArraylist.size();
        }

        @Override
        public String getItem(int position) {
            return patientInfoArraylist.get(position).getPaient_id();
        }

        public String getItemID(int position) {
            String query = "Select * From " + PatientDatabaseHelper.TABLE_NAME + ";";
            cursor = db.rawQuery(query, null);
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(PatientDatabaseHelper.COLUMN_ID));
        }


        public int getId(int position) {
            return position;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 13 && resultCode == 12) {
            Bundle retrival;
            retrival = data.getExtras();

            String dbid = retrival.getString("IDforDbDeletion");
            long listviewPosition = retrival.getLong("returned_ListViewPosition");
            String query = "DELETE FROM " + PatientDatabaseHelper.TABLE_NAME + " WHERE " + PatientDatabaseHelper.COLUMN_ID + " = "+ dbid;
            db.execSQL(query);
            patientInfoArraylist.remove((int)listviewPosition);

            patientListAdapter.notifyDataSetChanged();
            Log.i("DELETED A RECORD","Record deleted");


        }

    }
}