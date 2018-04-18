package com.example.chen.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailFragment extends Fragment {

    Bundle bundle;
    PatientDatabaseHelper patientDatabaseHelper;
    SQLiteDatabase db;
    Context ctx;

    Button intakeForm_ButtonDelete;
    Button intakeForm_ButtonUpdate;
    Button intakeForm_ButtonConfirm;


    EditText intakeform_PatientFragment_Item_PN;
    EditText intakeform_PatientFragment_Item_Address;
    EditText intakeform_PatientFragment_Item_Birthday;
    EditText intakeform_PatientFragment_Item_Phone;
    EditText intakeform_PatientFragment_Item_Healthcard;
    EditText intakeform_PatientFragment_Item_Description;
    EditText intakeform_PatientFragment_Item_SP1;
    EditText intakeform_PatientFragment_Item_SP2;


    public DetailFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View newView = inflater.inflate(R.layout.fragment_detail, container, false);

        intakeForm_ButtonDelete = newView.findViewById(R.id.IntakeForm_ButtonDelete);
        intakeForm_ButtonUpdate = newView.findViewById(R.id.IntakeForm_buttonUpdate);
        intakeForm_ButtonConfirm = newView.findViewById(R.id.IntakeForm_buttonConfirm);


        intakeform_PatientFragment_Item_PN = newView.findViewById(R.id.intakeform_PatientFragment_Item_PN);
        intakeform_PatientFragment_Item_Address = newView.findViewById(R.id.intakeform_PatientFragment_Item_Address);
        intakeform_PatientFragment_Item_Birthday = newView.findViewById(R.id.intakeform_PatientFragment_Item_Birthday);
        intakeform_PatientFragment_Item_Phone = newView.findViewById(R.id.intakeform_PatientFragment_Item_Phone);
        intakeform_PatientFragment_Item_Healthcard = newView.findViewById(R.id.intakeform_PatientFragment_Item_Healthcard);
        intakeform_PatientFragment_Item_Description = newView.findViewById(R.id.intakeform_PatientFragment_Item_Description);
        intakeform_PatientFragment_Item_SP1 = newView.findViewById(R.id.intakeform_PatientFragment_Item_SP1);
        intakeform_PatientFragment_Item_SP2 = newView.findViewById(R.id.intakeform_PatientFragment_Item_SP2);

        //Use the Singleton to connect to the database
        patientDatabaseHelper = PatientDatabaseHelper.getInstance(ctx);
        db = patientDatabaseHelper.getWritableDatabase();

        bundle = getArguments();
        //setting up the editText fields with info from bundle
        intakeform_PatientFragment_Item_PN.setText(bundle.getString("Patient_Name"));
        intakeform_PatientFragment_Item_Address.setText(bundle.getString("Patient_Address"));
        intakeform_PatientFragment_Item_Birthday.setText(bundle.getString("Patient_Birthday"));
        intakeform_PatientFragment_Item_Phone.setText(bundle.getString("Patient_Phone"));
        intakeform_PatientFragment_Item_Healthcard.setText(bundle.getString("Patient_Healthcard"));
        intakeform_PatientFragment_Item_Description.setText(bundle.getString("Patient_Description"));
        intakeform_PatientFragment_Item_SP1.setText(bundle.getString("Patient_SP1"));
        intakeform_PatientFragment_Item_SP2.setText(bundle.getString("Patient_SP2"));


        intakeForm_ButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("IDforDbDeletion",bundle.getString("Patient_ID"));
                returnIntent.putExtra("returned_ListViewPosition",bundle.getInt("Patient_Listview_Position"));
                getActivity().setResult(12,returnIntent);
                getActivity().finish();
            }
        });

        intakeForm_ButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intakeform_PatientFragment_Item_PN.setFocusable(true);
                intakeform_PatientFragment_Item_Address.setFocusable(true);
                intakeform_PatientFragment_Item_Birthday.setFocusable(true);
                intakeform_PatientFragment_Item_Phone.setFocusable(true);
                intakeform_PatientFragment_Item_Healthcard.setFocusable(true);
                intakeform_PatientFragment_Item_Description.setFocusable(true);
                intakeform_PatientFragment_Item_SP1.setFocusable(true);
                intakeform_PatientFragment_Item_SP2.setFocusable(true);

                intakeForm_ButtonConfirm.setVisibility(View.VISIBLE);

                Toast toast1 = Toast.makeText(getActivity(),"You can update the information now", Toast.LENGTH_LONG);
                toast1.show();

                intakeForm_ButtonUpdate.setVisibility(View.INVISIBLE);
                intakeForm_ButtonDelete.setVisibility(View.INVISIBLE);

                }
        });

        intakeForm_ButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues newData = new ContentValues();

                newData.put(PatientDatabaseHelper.COLUMN_NAME,intakeform_PatientFragment_Item_PN.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_ADDRESS,intakeform_PatientFragment_Item_Address.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_BIRTHDAY, intakeform_PatientFragment_Item_Birthday.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_PHONE, intakeform_PatientFragment_Item_Phone.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_HEALTHCARD, intakeform_PatientFragment_Item_Healthcard.getText().toString());
                newData.put(PatientDatabaseHelper.COLUMN_DESCRIPTION,intakeform_PatientFragment_Item_Description.getText().toString());
                if(bundle.getInt("Patient_Flag")==1) {
                    newData.put(PatientDatabaseHelper.COLUMN_PREVIOUSSURGERY, intakeform_PatientFragment_Item_SP1.getText().toString());
                    newData.put(PatientDatabaseHelper.COLUMN_ALLERGIES, intakeform_PatientFragment_Item_SP2.getText().toString());
                }else if(bundle.getInt("Patient_Flag")==2){
                    newData.put(PatientDatabaseHelper.COLUMN_BENEFITS, intakeform_PatientFragment_Item_SP1.getText().toString());
                    newData.put(PatientDatabaseHelper.COLUMN_HADBRACES,intakeform_PatientFragment_Item_SP2.getText().toString());
                }else if(bundle.getInt("Patient_Flag")==3){
                    newData.put(PatientDatabaseHelper.COLUMN_GLASSESBOUGHT, intakeform_PatientFragment_Item_SP1.getText().toString());
                    newData.put(PatientDatabaseHelper.COLUMN_GLASSESSTORE, intakeform_PatientFragment_Item_SP2.getText().toString());
                }

                db.update(PatientDatabaseHelper.TABLE_NAME,newData,PatientDatabaseHelper.COLUMN_ID + " = "+bundle.getString("Patient_ID"),null);

                Toast toast2 = Toast.makeText(getActivity(),"Information has been updated", Toast.LENGTH_LONG);
                toast2.show();

                intakeform_PatientFragment_Item_Address.setFocusable(false);
                intakeform_PatientFragment_Item_Birthday.setFocusable(false);
                intakeform_PatientFragment_Item_Phone.setFocusable(false);
                intakeform_PatientFragment_Item_Healthcard.setFocusable(false);
                intakeform_PatientFragment_Item_Description.setFocusable(false);
                intakeform_PatientFragment_Item_SP1.setFocusable(false);
                intakeform_PatientFragment_Item_SP2.setFocusable(false);

                intakeForm_ButtonConfirm.setVisibility(View.INVISIBLE);
                intakeForm_ButtonUpdate.setVisibility(View.VISIBLE);
                intakeForm_ButtonDelete.setVisibility(View.VISIBLE);

            }
        });

        return newView;
    }


}
