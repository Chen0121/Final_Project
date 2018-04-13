package com.example.chen.final_project;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chen.final_project.R;

public class multipleFragment extends Fragment {
    private boolean isTablet;
    private CheckBox checkA;
    private CheckBox checkB;
    private CheckBox checkC;
    private CheckBox checkD;
    private EditText textA;
    private EditText textB;
    private EditText textC;
    private EditText textD;
    private EditText multipleQuestion;
    private EditText multipleCorrect;
    private Button btn_update;
    private Button btn_delete;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.multiple_fragment,container,false);
        multipleQuestion=view.findViewById(R.id.question_fragment);
        textA=view.findViewById(R.id.txt_A);
        textB=view.findViewById(R.id.txt_B);
        textC=view.findViewById(R.id.txt_C);
        textD=view.findViewById(R.id.txt_D);
        checkA=view.findViewById(R.id.A_fragment);
        checkB=view.findViewById(R.id.B_fragment);
        checkC=view.findViewById(R.id.C_fragment);
        checkD=view.findViewById(R.id.D_fragment);
        btn_delete=view.findViewById(R.id.delete);
        btn_update=view.findViewById(R.id.update);
        bundle=getArguments();

        final String question=bundle.getString("Question");
        String answerA=bundle.getString("answerA");
        String answerB=bundle.getString("answerB");
        String answerC=bundle.getString("answerC");
        String answerD=bundle.getString("answerD");
        final String correct=bundle.getString("correct");
        final long id=bundle.getLong("ID");
        final long id_inChat = bundle.getLong("IDInChat");

        multipleQuestion.setText(question);
        textA.setText(answerA);
        textB.setText(answerB);
        textC.setText(answerC);
        textD.setText(answerD);
        multipleCorrect.setText(correct);

        if(correct.equals("A")){
            checkA.setChecked(true);
        }else if(correct.equals("B")){
            checkB.setChecked(true);
        }else if(correct.equals("C")){
            checkC.setChecked(true);
        }else if(correct.equals("D")){
            checkD.setChecked(true);
        }else{
            throw new IllegalArgumentException("checkbox did not match");
        }

        checkA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkB.setChecked(false);
                checkC.setChecked(false);
                checkD.setChecked(false);
            }
        });
        checkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkA.setChecked(false);
                checkC.setChecked(false);
                checkD.setChecked(false);
            }
        });
        checkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkA.setChecked(false);
                checkB.setChecked(false);
                checkD.setChecked(false);
            }
        });
        checkD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkA.setChecked(false);
                checkB.setChecked(false);
                checkC.setChecked(false);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTablet){
                    CreateQuiz createQuiz=(CreateQuiz)getActivity();
                    createQuiz.deleteForTablet(id,id_inChat);
                    getFragmentManager().beginTransaction().remove(multipleFragment.this).commit();
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("action", 1);
                    Intent deleteID = resultIntent.putExtra("DeleteID", id);
                    resultIntent.putExtra("IDInChat", id_inChat);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newQuestion=multipleQuestion.getText().toString();
                String newA=textA.getText().toString();
                String newB=textB.getText().toString();
                String newC=textC.getText().toString();
                String newD=textD.getText().toString();
                String newCorrect = null;
                if(checkA.isChecked()){
                    newCorrect="A";
                }else if(checkB.isChecked()){
                    newCorrect="B";
                }else if(checkC.isChecked()){
                    newCorrect="C";
                }else if(checkD.isChecked()){
                    newCorrect="D";
                }

                if(isTablet){
                    CreateQuiz createQuiz=(CreateQuiz)getActivity();
                    createQuiz.deleteForTablet(id, id_inChat);
                    createQuiz.updateForTablet(newA, newB, newC, newD, newQuestion, newCorrect);
                    getFragmentManager().beginTransaction().remove(multipleFragment.this).commit();
                }else {
                    Intent resultIntent=new Intent();
                    resultIntent.putExtra("type", 1);
                    resultIntent.putExtra("action", 2);
                    resultIntent.putExtra("NewQuestion",newQuestion);
                    resultIntent.putExtra("AnswerA",newA);
                    resultIntent.putExtra("AnswerB",newB);
                    resultIntent.putExtra("AnswerC",newC);
                    resultIntent.putExtra("AnswerD",newD);
                    resultIntent.putExtra("NewCorrect",newCorrect);
                    resultIntent.putExtra("UpdateID", id);
                    resultIntent.putExtra("IDInChat", id_inChat);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    public void setIsTablet(boolean isTablet){

        this.isTablet=isTablet;
    }

}
