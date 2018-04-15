package com.example.chen.final_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

import java.util.ArrayList;

import static com.example.chen.final_project.QuizDatabaseHelper.KEY_A;
import static com.example.chen.final_project.QuizDatabaseHelper.KEY_B;
import static com.example.chen.final_project.QuizDatabaseHelper.KEY_C;
import static com.example.chen.final_project.QuizDatabaseHelper.KEY_Correct;
import static com.example.chen.final_project.QuizDatabaseHelper.KEY_D;
import static com.example.chen.final_project.QuizDatabaseHelper.KEY_Question;
import static com.example.chen.final_project.QuizDatabaseHelper.table_multiple;

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
    private Cursor cursor;
    private SQLiteDatabase db;
    private String query;
    private ArrayList<Question> questionArray = new ArrayList<>();
    private CreateQuiz.QuestionAdapter questionAdapter;

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

        checkA=view.findViewById(R.id.A_fragment);
        checkB=view.findViewById(R.id.B_fragment);
        checkC=view.findViewById(R.id.C_fragment);
        checkD=view.findViewById(R.id.D_fragment);
        btn_delete=view.findViewById(R.id.delete);
        btn_update=view.findViewById(R.id.update);

        bundle = getArguments();

        String answerA= bundle.getString("answerA");
        String answerB= bundle.getString("answerB");
        String answerC= bundle.getString("answerC");
        String answerD= bundle.getString("answerD");
        String question=bundle.getString("Question");
        String correct= bundle.getString("correct");
        final long id= bundle.getLong("ID");
        final long id_inChat = bundle.getLong("IDInChat");

        textA.setText(answerA);
        textB.setText(answerB);
        textC.setText(answerC);
        textD.setText(answerD);
        multipleQuestion.setText(question);
        multipleCorrect.setText(correct);

        btn_delete.setOnClickListener(view15 -> {
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
                create.deleteForTablet(id, id_inChat);
                create.updateForTablet(newA, newB, newC, newD, newQuestion, newCorrect);
                getFragmentManager().beginTransaction().remove(multipleFragment.this).commit();
            } else {
                Intent intent = new Intent();
                intent.putExtra("action", 2);
                intent.putExtra("Question",newQuestion);
                intent.putExtra("answerA", newA);
                intent.putExtra("answerB", newB);
                intent.putExtra("answerC", newC);
                intent.putExtra("answerD", newD);
                intent.putExtra("correct", newCorrect);
                intent.putExtra("UpdateID", id);
                intent.putExtra("IDInChat", id_inChat);
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
