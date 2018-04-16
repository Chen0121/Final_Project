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
import android.widget.CheckBox;
import android.widget.EditText ;
import android.widget.ListView ;
import android.widget.TextView ;

import java.util.ArrayList ;

import static com.example.chen.final_project.QuizDatabaseHelper.*;

public class CreateQuiz extends AppCompatActivity {
    private Cursor cursor;
    private String query;
    private QuizDatabaseHelper dbHelper = new QuizDatabaseHelper(this);
    private SQLiteDatabase db;
    private boolean isTablet;
    private boolean isTrue;
    private Question Question;
    private ArrayList<Question> questionArray = new ArrayList<>();
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);
        db = dbHelper.getWritableDatabase();
        isTablet = (findViewById(R.id.frame_layout) != null);

        ListView list_multiple = findViewById(R.id.list_1);

        Button btn_multiple = findViewById(R.id.button_multiple);
        Button btn_tf=findViewById(R.id.button_tf);
       
        questionAdapter = new QuestionAdapter(this);
        list_multiple.setAdapter(questionAdapter);

        query = "SELECT * FROM " + QuizDatabaseHelper.table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(bundle.getString("QuestionType").equals("multiple")) {
                String answerA = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_A));
                String answerB = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_B));
                String answerC = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_C));
                String answerD = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_D));
                String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                String correct = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct));
                Question = new multipleQuestion(answerA, answerB, answerC, answerD, question, correct);
            }else if (bundle.getString("QuestionType").equals("tf")) {
                String que = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                boolean answer = cursor.getInt(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct)) == 1;
                Question = new tfQuestion(answer, que);
            }
//            else if (bundle.getString("QuestionType").equals("numeric")){
//
//            }
            questionArray.add(Question);
            cursor.moveToNext();
        }

        btn_multiple.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuiz.this);
            final View view = CreateQuiz.this.getLayoutInflater().inflate(R.layout.multiple_layout, null);
            builder.setView(view);
            builder.setPositiveButton(R.string.ADD_multiple, (dialog, id) -> {
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

                db.insert(table_name, "", cv);
                query = "SELECT * FROM " + table_name + ";";
                cursor = db.rawQuery(query, null);

                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton(R.string.CANCEL_multiple, (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        btn_tf.setOnClickListener((View e) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuiz.this);
            final View view = CreateQuiz.this.getLayoutInflater().inflate(R.layout.tf_layout, null);
            builder.setView(view);
            builder.setPositiveButton(R.string.ADD_tf, (DialogInterface dialog, int id) -> {
                EditText question = view.findViewById(R.id.tf_fragment);
                CheckBox checkA=view.findViewById(R.id.A_f);
                CheckBox checkB=view.findViewById(R.id.B_f);
                if(checkA.isChecked()){
                    isTrue=true;
                }else if(checkB.isChecked()) {
                    isTrue = false;
                }
                String que = question.getText().toString();
                tfQuestion tfQuestion=new tfQuestion(isTrue,que);
                questionArray.add(tfQuestion);

                ContentValues cv = new ContentValues();
                cv.put(KEY_Question, que);
                cv.put(KEY_Correct, isTrue);

                db.insert(table_name, "", cv);
                query = "SELECT * FROM " + table_name + ";";
                cursor = db.rawQuery(query, null);

                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();
                });
                builder.setNegativeButton(R.string.CANCEL_multiple, (dialog, id) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });

        list_multiple.setOnItemClickListener((adapterView, view, position, id) -> {
            if (bundle.getString("QuestionType").equals("multiple")) {
                String a1 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerA();
                String a2 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerB();
                String a3 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerC();
                String a4 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerD();
                String q = questionAdapter.getItem(position).getQuestion();
                String c = ((multipleQuestion) questionAdapter.getItem(position)).getCorrect();
                long id_inList = questionAdapter.getId(position);
                multipleFragment Fragment = new multipleFragment();

                Bundle bundle = new Bundle();
                bundle.putString("answerA", a1);
                bundle.putString("answerB", a2);
                bundle.putString("answerC", a3);
                bundle.putString("answerD", a4);
                bundle.putString("Question", q);
                bundle.putString("correct", c);
                bundle.putLong("LIST", id_inList);
                bundle.putLong("ID", id);

                if (isTablet) {
                    Fragment.setArguments(bundle);
                    Fragment.setIsTablet(true);
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, Fragment).commit();
                } else {
                    Fragment.setIsTablet(false);
                    Intent multiDetails = new Intent(CreateQuiz.this, multipleDetails.class);
                    multiDetails.putExtra("Question", bundle);
                    startActivityForResult(multiDetails, 1, bundle);
                }
            } else if () {
            }else if(){


        }
        
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle b = data.getExtras();
            if (b.getInt("choice") == 1) {
                long id = b.getLong("deleteID");
                long id_inList = b.getLong("LIST");
                db.delete(table_name, KEY_ID + " = ?", new String[]{Long.toString(id)});
                questionArray.remove((int) id_inList);
                query = "SELECT * FROM " + table_name + ";";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();
            } else if (b.getInt("choice") == 2) {
                long id = b.getLong("UpdateID");
                long id_inList = b.getLong("LIST");
                db.delete(table_name, KEY_ID + " = ?", new String[]{Long.toString(id)});
                questionArray.remove((int) id_inList);
                query = "SELECT * FROM " + table_name + ";";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();

                String ans1 = b.getString("answerA");
                String ans2 = b.getString("answerB");
                String ans3 = b.getString("answerC");
                String ans4 = b.getString("answerD");
                String question = b.getString("Question");
                String correct = b.getString("correct");
                updateForTablet(ans1, ans2, ans3, ans4, question, correct);
            }
        }
    }

    public void updateForTablet(String ans1, String ans2, String ans3, String ans4, String question, String correct) {
        String answer1 = ans1;
        String answer2 = ans2;
        String answer3 = ans3;
        String answer4 = ans4;
        String que = question;
        String c = correct;

        Question q = new multipleQuestion(answer1, answer2, answer3, answer4, que, c);
        questionArray.add(q);

        ContentValues cv = new ContentValues();
        cv.put(KEY_A, answer1);
        cv.put(KEY_B, answer2);
        cv.put(KEY_C, answer3);
        cv.put(KEY_D, answer4);
        cv.put(KEY_Question, que);
        cv.put(KEY_Correct, c);

        db.insert(table_name,"",cv);
        query = "SELECT * FROM " + table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    public void deleteForTablet(long idInDatabase, long idInList) {
        String query;
        long id = idInDatabase;
        long id_inList = idInList;
        db.delete(table_name, KEY_ID + " = ?", new String[]{Long.toString(id)});
        questionArray.remove((int) id_inList);
        query = "SELECT * FROM " + table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    }
    class QuestionAdapter extends ArrayAdapter<Question> {

        private QuestionAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){ return questionArray.size();}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result;
            TextView Txt;
            result = CreateQuiz.this.getLayoutInflater().inflate(R.layout.question, null);
            Txt = result.findViewById(R.id.text_view);
            Txt.setText(getItem(position).getQuestion());
            return result;
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex("id"));
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