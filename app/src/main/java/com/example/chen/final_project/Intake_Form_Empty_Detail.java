package com.example.chen.final_project;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class Intake_Form_Empty_Detail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_empty_detail);

        Bundle bundleInfo= getIntent().getBundleExtra("PatientInfo");
        DetailFragment patientFragment = new DetailFragment();
        patientFragment.setArguments(bundleInfo);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.IntakeForm_EmptyLayout, patientFragment);
        transaction.commit();


    }
}
