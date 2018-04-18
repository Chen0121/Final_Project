package com.example.chen.final_project;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class tfFragment extends Fragment {
    private EditText tfQuestion;
    private CheckBox checkA;
    private CheckBox checkB;
    private boolean isTablet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.tf_fragment,container,false);
        tfQuestion=view.findViewById(R.id.tf_fragment);
        checkA=view.findViewById(R.id.A_f);
        checkB=view.findViewById(R.id.B_f);
        Button btn_delete = view.findViewById(R.id.deletetf);
        Button btn_update = view.findViewById(R.id.updatetf);

        Bundle bundle = getArguments();
        String tfquestion= bundle.getString("Question");
        boolean tfanswer= bundle.getBoolean("answer");
        final long id= bundle.getLong("ID");
        final long list = bundle.getLong("LIST");

        tfQuestion.setText(tfquestion);
        if(tfanswer){
            checkA.setChecked(true);
        }else {
            checkB.setChecked(true);

        }

        btn_delete.setOnClickListener(view15 -> {
            if(isTablet){
                CreateQuiz createQuiz=(CreateQuiz)getActivity();
                createQuiz.deleteForTablet(id,list);
                getFragmentManager().beginTransaction().remove(tfFragment.this).commit();
            }else{
                Intent resultIntent = new Intent();
                resultIntent.putExtra("choice", 1);
                resultIntent.putExtra("deleteID", id);
                resultIntent.putExtra("LIST", list);
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
            }
        });

        btn_update.setOnClickListener((View view16) -> {
            String newQuestion=tfQuestion.getText().toString();
            String newCor ="";
            if(checkA.isChecked()){
                newCor = "1";
            }else if(checkB.isChecked()){
                newCor = "2";
            }
            if (isTablet) {
                CreateQuiz create = (CreateQuiz) getActivity();
                create.deleteForTablet(id, list);
                create.updateForTF(newQuestion,newCor);
                getFragmentManager().beginTransaction().remove(tfFragment.this).commit();
            } else {

                Intent intent = new Intent();
                intent.putExtra("choice", 2);
                intent.putExtra("Question",newQuestion);
                intent.putExtra("answer", newCor);
                intent.putExtra("UpdateID", id);
                intent.putExtra("LIST", list);
                intent.putExtra("type", "tf");
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }

    public void setIsTablet(boolean isTablet){

        this.isTablet=isTablet;
    }
}
