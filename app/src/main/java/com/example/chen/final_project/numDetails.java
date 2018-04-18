package com.example.chen.final_project;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class numDetails extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_details);

        Bundle bundle = getIntent().getBundleExtra("Question");
        tfFragment Fragment = new tfFragment();
        Fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, Fragment).commit();
    }
}
