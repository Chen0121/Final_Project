package com.example.chen.final_project;

import android.app.Activity ;
import android.content.ContentValues ;
import android.content.Context ;
import android.content.DialogInterface ;
import android.content.Intent ;
import android.database.Cursor ;
import android.database.sqlite.SQLiteDatabase ;
import android.support.v7.app.AlertDialog ;
import android.support.v7.app.AppCompatActivity ;
import android.os.Bundle ;
import android.view.View ;
import android.view.ViewGroup ;
import android.widget.ArrayAdapter;
import android.widget.Button ;
import android.widget.EditText ;
import android.widget.ListView ;
import android.widget.TextView ;
import android.widget.Toast ;

import java.util.ArrayList ;

import static com.example.chen.final_project.QuizDatabaseHelper.*;

public class CreateQuiz extends AppCompatActivity {

    private ListView list_multiple;
    private Button btn_multiple;
    private Cursor cursor;
    private String query;
    private QuizDatabaseHelper dbHelper = new QuizDatabaseHelper(this);
    private SQLiteDatabase db = null;
    private boolean isTablet;
    private Question Question;
    private ArrayList<Question> questionArray = new ArrayList<>();
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);
        db = dbHelper.getWritableDatabase();
        isTablet = (findViewById(R.id.framelayout) != null);

        list_multiple = findViewById(R.id.list_1);
//        list_tf = findViewById(R.id.list_2);
//        list_numeric = findViewById(R.id.list_3);

        btn_multiple = findViewById(R.id.button_multiple);
//        btn_tf = findViewById(R.id.button_tf);
//        btn_numeric = findViewById(R.id.button_numeric);

        questionAdapter = new QuestionAdapter(this);
        list_multiple.setAdapter(questionAdapter);
//        list_tf.setAdapter(questionAdapter);
//        list_numeric.setAdapter(questionAdapter);

        query = "SELECT * FROM " + table_multiple + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String answerA = cursor.getString(cursor.getColumnIndex(KEY_A));
            String answerB = cursor.getString(cursor.getColumnIndex(KEY_B));
            String answerC = cursor.getString(cursor.getColumnIndex(KEY_C));
            String answerD = cursor.getString(cursor.getColumnIndex(KEY_D));
            String question = cursor.getString(cursor.getColumnIndex(KEY_Question));
            String correct = cursor.getString(cursor.getColumnIndex(KEY_Correct));
            Question = new multipleQuestion(answerA, answerB, answerC, answerD, question, correct);
            questionArray.add(Question);
            cursor.moveToNext();
        }

        btn_multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuiz.this);
                final View view = CreateQuiz.this.getLayoutInflater().inflate(R.layout.multiple_layout, null);
                builder.setView(view);
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText a = view.findViewById(R.id.txt_A);
                        EditText b = view.findViewById(R.id.txt_B);
                        EditText c = view.findViewById(R.id.txt_C);
                        EditText d = view.findViewById(R.id.txt_D);
                        EditText question = view.findViewById(R.id.question_fragment);
                        EditText cor = view.findViewById(R.id.correct);
                        String ansA = a.getText().toString();
                        String ansB = b.getText().toString();
                        String ansC = c.getText().toString();
                        String ansD = d.getText().toString();
                        String que = question.getText().toString();
                        String correct = cor.getText().toString();
                        multipleQuestion mQuestion = new multipleQuestion(ansA, ansB, ansC, ansD, que, correct);
                        questionArray.add(mQuestion);

                        ContentValues cv = new ContentValues();
                        cv.put(KEY_A, ansA);
                        cv.put(KEY_B, ansB);
                        cv.put(KEY_C, ansC);
                        cv.put(KEY_D, ansD);
                        cv.put(KEY_Question, que);
                        cv.put(KEY_Correct, correct);

                        db.insert(table_multiple, "", cv);
                        query = "SELECT * FROM " + table_multiple + ";";
                        cursor = db.rawQuery(query, null);
                        cursor.moveToFirst();
                        questionAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.CANCEL_multiple, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        list_multiple.setOnItemClickListener((adapterView, view, position, id) -> {
            String q = ((multipleQuestion) questionAdapter.getItem(position)).getQuestion();
            String a1 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerA();
            String a2 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerB();
            String a3 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerC();
            String a4 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerD();
            String c = ((multipleQuestion) questionAdapter.getItem(position)).getCorrect();
            Long id_inList = questionAdapter.getId(position);
            long ID = id;
            multipleFragment Fragment = new multipleFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Answer1", a1);
            bundle.putString("Answer2", a2);
            bundle.putString("Answer3", a3);
            bundle.putString("Answer4", a4);
            bundle.putString("Question", q);
            bundle.putString("Correct", c);
            bundle.putLong("IDInChat", id_inList);
            bundle.putLong("ID", ID);

            if (isTablet) {
                Fragment.setArguments(bundle);
                Fragment.setIsTablet(true);
                getFragmentManager().beginTransaction().replace(R.id.framelayout, Fragment).commit();
            } else{
                Fragment.setIsTablet(false);
                Intent multiDetails = new Intent(CreateQuiz.this, multipleDetails.class);
                multiDetails.putExtra("QuestionItem", bundle);
                startActivityForResult(multiDetails, 1, bundle);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle b = data.getExtras();
            if (b.getInt("action") == 1) {
                long id = b.getLong("DeleteID");
                long id_inList = b.getLong("IDInChat");
                db.delete(table_multiple, KEY_ID + " = ?", new String[]{Long.toString(id)});
                questionArray.remove((int) id_inList);
                query = "SELECT * FROM " + table_multiple + ";";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();
                CharSequence text = "delete";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            } else if (b.getInt("action") == 2) {
                long id = b.getLong("UpdateID");
                long id_inList = b.getLong("IDInChat");
                db.delete(table_multiple, KEY_ID + " = ?", new String[]{Long.toString(id)});
                questionArray.remove((int) id_inList);
                query = "SELECT * FROM " + table_multiple + ";";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();

                String ans1 = b.getString("Ans1");
                String ans2 = b.getString("Ans2");
                String ans3 = b.getString("Ans3");
                String ans4 = b.getString("Ans4");
                String question = b.getString("Question");
                String correct = b.getString("Correct");
                updateForTablet(ans1, ans2, ans3, ans4, question, correct);
            }
        }
    }

    public void updateForTablet(String ans1, String ans2, String ans3, String ans4, String question, String correct) {
        String que = question;
        String answer1 = ans1;
        String answer2 = ans2;
        String answer3 = ans3;
        String answer4 = ans4;
        String c = correct;
        Question q = new multipleQuestion(answer1, answer2, answer3, answer4, que, c);
        questionArray.add(q);
        ContentValues cv = new ContentValues();
//        cv.put(QuizDatabaseHelper.KEY_TYPE, 1);
        cv.put(KEY_A, answer1);
        cv.put(KEY_B, answer2);
        cv.put(KEY_C, answer3);
        cv.put(KEY_D, answer4);
        cv.put(KEY_Question, que);
        cv.put(KEY_Correct, c);
        db.insert(table_multiple,"",cv);
        query = "SELECT * FROM " + table_multiple + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    public void deleteForTablet(long idInDatabase, long idInList) {
        String query;
        long id = idInDatabase;
        long id_inList = idInList;
        db.delete(table_multiple, KEY_ID + " = ?", new String[]{Long.toString(id)});
        questionArray.remove((int) id_inList);
        query = "SELECT * FROM " + table_multiple + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private class QuestionAdapter extends ArrayAdapter<Question> {

        private QuestionAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result;
            TextView Txt;
            result = CreateQuiz.this.getLayoutInflater().inflate(R.layout.multiple_fragment, null);
            Txt = result.findViewById(R.id.question_txt);
            Txt.setText(getItem(position).getQuestion());
            return result;
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex("ID"));
        }

        @Override
        public Question getItem(int position) {
            return questionArray.get(position);
        }


        public long getId(int position) {
            return position;
        }


    }
}