package com.example.chen.final_project;

import android.app.Activity ;
import android.content.ContentValues ;
import android.content.Context ;
import android.content.DialogInterface;
import android.content.Intent ;
import android.database.Cursor ;
import android.database.sqlite.SQLiteDatabase ;
import android.support.v7.app.AlertDialog ;
import android.support.v7.app.AppCompatActivity ;
import android.os.Bundle;
import android.util.Log;
import android.view.View ;
import android.view.ViewGroup ;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button ;
import android.widget.CheckBox;
import android.widget.EditText ;
import android.widget.ListView ;
import android.widget.ProgressBar;
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
    private Bundle bundle;
    private ProgressBar progressBar;
    private TextView bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);
        db = dbHelper.getWritableDatabase();
        isTablet = (findViewById(R.id.frame_layout) != null);
        ListView list_multiple = findViewById(R.id.list_1);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        bar = (TextView) findViewById(R.id.bar);
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < 100) {
                    i++;
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int j = i;
                    progressBar.setProgress(i);
                    runOnUiThread(() -> bar.setText(j + "%"));
                }
            }
        }.start();

        Button btn_multiple = findViewById(R.id.button_multiple);
        Button btn_tf = findViewById(R.id.button_tf);
        Button btn_num = findViewById(R.id.button_numeric);

        questionAdapter = new QuestionAdapter(this);
        list_multiple.setAdapter(questionAdapter);

        query = "SELECT * FROM " + QuizDatabaseHelper.table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_TYPE)).equals("multipleChoice")) {
                String answerA = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_A));
                String answerB = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_B));
                String answerC = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_C));
                String answerD = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_D));
                String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                String correct = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct));
                Question = new multipleQuestion(answerA, answerB, answerC, answerD, question, correct);

            } else if (cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_TYPE)).equals("tf")) {
                String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                boolean answer = (cursor.getInt(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct)) == 1);
                Question = new tfQuestion(answer, question);

            } else if (cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_TYPE)).equals("numeric")) {
                String answerA = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_A));
                String answerB = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_B));
                String answerC = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_C));
                String answerD = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_D));
                String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                String correct = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct));
                String accuracy = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Accuracy));
                Question = new numQuestion(answerA, answerB, answerC, answerD, question, correct, accuracy);
            }else{
                Question = new multipleQuestion("null", "null", "null", "null", "null", "null");

            }
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
                cv.put(KEY_TYPE, "multipleChoice");

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
                CheckBox checkA = view.findViewById(R.id.A_f);
                CheckBox checkB = view.findViewById(R.id.B_f);
                if (checkA.isChecked()) {
                    isTrue = true;
                } else if (checkB.isChecked()) {
                    isTrue = false;
                }
                String tf_question = question.getText().toString();

                tfQuestion tfQuestion = new tfQuestion(isTrue, tf_question);
                questionArray.add(tfQuestion);

                ContentValues cv = new ContentValues();
                cv.put(KEY_Question, tf_question);
                cv.put(KEY_Correct, isTrue);
                cv.put(KEY_TYPE, "tf");

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

        btn_num.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuiz.this);
            final View view = CreateQuiz.this.getLayoutInflater().inflate(R.layout.numeric_layout, null);
            builder.setView(view);
            builder.setPositiveButton(R.string.ADD_multiple, (dialog, id) -> {
                EditText a = view.findViewById(R.id.txt_A);
                EditText b = view.findViewById(R.id.txt_B);
                EditText c = view.findViewById(R.id.txt_C);
                EditText d = view.findViewById(R.id.txt_D);
                EditText question = view.findViewById(R.id.question_fragment);
                EditText cor = view.findViewById(R.id.correct);
                EditText ac = view.findViewById(R.id.accuracyline);

                String ansA = a.getText().toString();
                String ansB = b.getText().toString();
                String ansC = c.getText().toString();
                String ansD = d.getText().toString();
                String que = question.getText().toString();
                String correct = cor.getText().toString();
                String accuracy = ac.getText().toString();

                numQuestion nQuestion = new numQuestion(ansA, ansB, ansC, ansD, que, correct, accuracy);
                questionArray.add(nQuestion);

                ContentValues cv = new ContentValues();
                cv.put(KEY_A, ansA);
                cv.put(KEY_B, ansB);
                cv.put(KEY_C, ansC);
                cv.put(KEY_D, ansD);
                cv.put(KEY_Question, que);
                cv.put(KEY_Correct, correct);
                cv.put(KEY_Accuracy, accuracy);
                cv.put(KEY_TYPE, "numeric");

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

        list_multiple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("Frame exists: ", "?"+isTablet);
                if (questionAdapter.getItem(position).getType().equals("multipleChoice")) {
                    String a1 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerA();
                    String a2 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerB();
                    String a3 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerC();
                    String a4 = ((multipleQuestion) questionAdapter.getItem(position)).getAnswerD();
                    String q = questionAdapter.getItem(position).getQuestion();
                    String c = ((multipleQuestion) questionAdapter.getItem(position)).getCorrect();
                    long id_inList = questionAdapter.getId(position);
                    multipleFragment Fragment = new multipleFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("AnswerA", a1);
                    bundle.putString("AnswerB", a2);
                    bundle.putString("AnswerC", a3);
                    bundle.putString("AnswerD", a4);
                    bundle.putString("Question", q);
                    bundle.putString("correct", c);
                    bundle.putLong("LIST", id_inList);
                    bundle.putLong("ID", id);
                    bundle.putString("type", "multipleChoice");

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
                } else if (questionAdapter.getItem(position).getType().equals("tf")) {
                    String question = questionAdapter.getItem(position).getQuestion();
                    Boolean isR = ((tfQuestion) questionAdapter.getItem(position)).isRight();

                    long id_inList = questionAdapter.getId(position);
                    bundle = new Bundle();
                    tfFragment Fragment = new tfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "tf");
                    bundle.putString("Question", question);
                    bundle.putBoolean("answer", isR);
                    bundle.putLong("LIST", id_inList);
                    bundle.putLong("ID", id);

                    if (isTablet) {
                        Fragment.setArguments(bundle);
                        Fragment.setIsTablet(true);
                        getFragmentManager().beginTransaction().replace(R.id.frame_layout, Fragment).commit();
                    } else {
                        Fragment.setIsTablet(false);
                        Intent tfDetails = new Intent(CreateQuiz.this, tfDetails.class);
                        tfDetails.putExtra("Question", bundle);
                        startActivityForResult(tfDetails, 1, bundle);
                    }
                } else if (questionAdapter.getItem(position).getType().equals("numeric")) {
                    String a1 = ((numQuestion) questionAdapter.getItem(position)).getAnswerA();
                    String a2 = ((numQuestion) questionAdapter.getItem(position)).getAnswerB();
                    String a3 = ((numQuestion) questionAdapter.getItem(position)).getAnswerC();
                    String a4 = ((numQuestion) questionAdapter.getItem(position)).getAnswerD();
                    String q = questionAdapter.getItem(position).getQuestion();
                    String c = ((numQuestion) questionAdapter.getItem(position)).getCorrect();
                    String ac = ((numQuestion) questionAdapter.getItem(position)).getAccuracy();
                    long id_inList = questionAdapter.getId(position);
                    numFragment Fragment = new numFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("AnswerA", a1);
                    bundle.putString("AnswerB", a2);
                    bundle.putString("AnswerC", a3);
                    bundle.putString("AnswerD", a4);
                    bundle.putString("Question", q);
                    bundle.putString("correct", c);
                    bundle.putString("accuracy", ac);
                    bundle.putLong("LIST", id_inList);
                    bundle.putLong("ID", id);
                    bundle.putString("type", "numeric");

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
                }
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
                Log.i("FART", b.getString("type"));
                db.delete(table_name, KEY_ID + " = ?", new String[]{Long.toString(id)});
                questionArray.remove((int) id_inList);
                query = "SELECT * FROM " + table_name + ";";
                cursor = db.rawQuery(query, null);
                cursor.moveToFirst();
                questionAdapter.notifyDataSetChanged();

                if (b.getString("type").equals("multipleChoice")) {
                    String ans1 = b.getString("AnswerA");
                    String ans2 = b.getString("AnswerB");
                    String ans3 = b.getString("AnswerC");
                    String ans4 = b.getString("AnswerD");
                    String question = b.getString("Question");
                    String correct = b.getString("correct");
                    updateForTablet(ans1, ans2, ans3, ans4, question, correct);
                } else if (b.getString("type").equals("tf")) {
                    String question = b.getString("Question");
                    String answer = b.getString("answer");
                    updateForTF(question, answer);
                } else if (b.getString("type").equals("numeric")) {
                    String ans1 = b.getString("AnswerA");
                    String ans2 = b.getString("AnswerB");
                    String ans3 = b.getString("AnswerC");
                    String ans4 = b.getString("AnswerD");
                    String question = b.getString("Question");
                    String correct = b.getString("correct");
                    String accuracy = b.getString("accuracy");
                    updateForNum(ans1, ans2, ans3, ans4, question, correct, accuracy);
                }
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
        cv.put(KEY_TYPE, "multipleChoice");


        db.insert(table_name, "", cv);
        query = "SELECT * FROM " + table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    public void updateForNum(String ans1, String ans2, String ans3, String ans4, String question, String correct, String accuracy) {
        String answer1 = ans1;
        String answer2 = ans2;
        String answer3 = ans3;
        String answer4 = ans4;
        String que = question;
        String c = correct;
        String acc = accuracy;

        Question q = new numQuestion(answer1, answer2, answer3, answer4, que, c, acc);
        questionArray.add(q);

        ContentValues cv = new ContentValues();
        cv.put(KEY_A, answer1);
        cv.put(KEY_B, answer2);
        cv.put(KEY_C, answer3);
        cv.put(KEY_D, answer4);
        cv.put(KEY_Question, que);
        cv.put(KEY_Correct, c);
        cv.put(KEY_Accuracy, acc);
        cv.put(KEY_TYPE, "numeric");

        db.insert(table_name, "", cv);
        query = "SELECT * FROM " + table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    public void updateForTF(String question, String answer) {
        String que = question;
        String ans = answer;
        Question q = new tfQuestion(ans == "true", que);
        questionArray.add(q);

        ContentValues cv = new ContentValues();
        cv.put(KEY_Question, que);
        cv.put(KEY_Correct, ans);
        cv.put(KEY_TYPE, "tf");

        db.insert(table_name, "", cv);
        query = "SELECT * FROM " + table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    public void deleteForTablet(long idDB, long idInList) {
        String query;
        long id = idDB;
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

    class QuestionAdapter extends ArrayAdapter<Question> {

        private QuestionAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return questionArray.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = null;
            TextView Txt;
            result = CreateQuiz.this.getLayoutInflater().inflate(R.layout.question, null);
            Txt = result.findViewById(R.id.text_view);
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



