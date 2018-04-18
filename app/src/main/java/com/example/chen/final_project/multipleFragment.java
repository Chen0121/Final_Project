package com.example.chen.final_project;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class multipleFragment extends Fragment {

    private EditText textA;
    private EditText textB;
    private EditText textC;
    private EditText textD;
    private EditText multipleQuestion;
    private EditText multipleCorrect;
    private boolean isTablet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view=inflater.inflate(R.layout.multiple_fragment,container,false);
        textA=view.findViewById(R.id.txt_A);
        textB=view.findViewById(R.id.txt_B);
        textC=view.findViewById(R.id.txt_C);
        textD=view.findViewById(R.id.txt_D);
        multipleQuestion=view.findViewById(R.id.question_fragment);
        multipleCorrect=view.findViewById(R.id.correct);


        Button btn_delete = view.findViewById(R.id.delete);
        Button btn_update = view.findViewById(R.id.update);

        Bundle bundle = getArguments();
        String answerA= bundle.getString("AnswerA");
        String answerB= bundle.getString("AnswerB");
        String answerC= bundle.getString("AnswerC");
        String answerD= bundle.getString("AnswerD");
        String question= bundle.getString("Question");
        String correct= bundle.getString("correct");
        final long id= bundle.getLong("ID");
        final long list = bundle.getLong("LIST");

        textA.setText(answerA);
        textB.setText(answerB);
        textC.setText(answerC);
        textD.setText(answerD);
        multipleQuestion.setText(question);
        multipleCorrect.setText(correct);

        btn_delete.setOnClickListener(view15 -> {
            if(isTablet){
                CreateQuiz createQuiz=(CreateQuiz)getActivity();
                createQuiz.deleteForTablet(id,list);
                getFragmentManager().beginTransaction().remove(multipleFragment.this).commit();
            }else{
                Intent resultIntent = new Intent();
                resultIntent.putExtra("choice", 1);
                Intent deleteID = resultIntent.putExtra("deleteID", id);
                resultIntent.putExtra("LIST", list);
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
            }
        });

        btn_update.setOnClickListener((View view16) -> {
            String newA=textA.getText().toString();
            String newB=textB.getText().toString();
            String newC=textC.getText().toString();
            String newD=textD.getText().toString();
            String newQuestion=multipleQuestion.getText().toString();
            String newCorrect =multipleCorrect.getText().toString();


            if (isTablet) {
                CreateQuiz create = (CreateQuiz) getActivity();
                create.deleteForTablet(id, list);
                create.updateForTablet(newA, newB, newC, newD, newQuestion, newCorrect);
                getFragmentManager().beginTransaction().remove(multipleFragment.this).commit();
            } else {
                Intent intent = new Intent();
                intent.putExtra("choice", 2);
                intent.putExtra("AnswerA", newA);
                intent.putExtra("AnswerB", newB);
                intent.putExtra("AnswerC", newC);
                intent.putExtra("AnswerD", newD);
                intent.putExtra("Question",newQuestion);
                intent.putExtra("correct", newCorrect);
                intent.putExtra("type","multipleChoice");
                intent.putExtra("UpdateID", id);
                intent.putExtra("LIST", list);
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
