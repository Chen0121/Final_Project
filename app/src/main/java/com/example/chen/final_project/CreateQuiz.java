package com.example.chen.final_project;

import android.app.Activity ;
import android.content.ContentValues ;
import android.content.Context ;
import android.content.DialogInterface;
import android.content.Intent ;
import android.database.Cursor ;
import android.database.sqlite.SQLiteDatabase ;
import android.os.AsyncTask;
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
import android.widget.TextView ;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList ;

import static android.provider.Contacts.SettingsColumns.KEY;
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
    private ListView list_multiple;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_quiz);
        db = dbHelper.getWritableDatabase();
        isTablet = (findViewById(R.id.frame_layout) != null);
        list_multiple = findViewById(R.id.list_1);

        Button btn_multiple = findViewById(R.id.button_multiple);
        Button btn_tf=findViewById(R.id.button_tf);
        Button btn_num=findViewById(R.id.button_numeric);

        questionAdapter = new QuestionAdapter(this);
        list_multiple.setAdapter(questionAdapter);

        query = "SELECT * FROM " + QuizDatabaseHelper.table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if(cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_TYPE)).equals("multipleChoice")) {
            String answerA = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_A));
            String answerB = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_B));
            String answerC = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_C));
            String answerD = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_D));
            String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
            String correct = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct));
            Question = new multipleQuestion(answerA, answerB, answerC, answerD, question, correct);
            } else if (cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_TYPE)).equals("tf")) {
                String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                boolean answer=(cursor.getInt(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct))==1);
                Question = new tfQuestion(answer, question);
            } else if (cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_TYPE)).equals("numeric")){
                String answerA = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_A));
                String answerB = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_B));
                String answerC = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_C));
                String answerD = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_D));
                String question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                String correct = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Correct));
                String accuracy=cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Accuracy));
                Question = new numQuestion(answerA, answerB, answerC, answerD, question, correct, accuracy);
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
                CheckBox checkA=view.findViewById(R.id.A_f);
                CheckBox checkB=view.findViewById(R.id.B_f);
                if(checkA.isChecked()){
                    isTrue=true;
                }else if(checkB.isChecked()) {
                    isTrue=false;
                }
                String tf_question = question.getText().toString();

                tfQuestion tfQuestion=new tfQuestion(isTrue,tf_question);
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
                EditText ac=view.findViewById(R.id.accuracyline);

                String ansA = a.getText().toString();
                String ansB = b.getText().toString();
                String ansC = c.getText().toString();
                String ansD = d.getText().toString();
                String que = question.getText().toString();
                String correct = cor.getText().toString();
                String accuracy=ac.getText().toString();

                numQuestion nQuestion = new numQuestion(ansA, ansB, ansC, ansD, que, correct,accuracy);
                questionArray.add(nQuestion);

                ContentValues cv = new ContentValues();
                cv.put(KEY_A, ansA);
                cv.put(KEY_B, ansB);
                cv.put(KEY_C, ansC);
                cv.put(KEY_D, ansD);
                cv.put(KEY_Question, que);
                cv.put(KEY_Correct, correct);
                cv.put(KEY_Accuracy,accuracy);
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
                bundle.putString("type","multipleChoice");

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
             else if (questionAdapter.getItem(position).getType().equals("tf")) {
                String question = questionAdapter.getItem(position).getQuestion();
                Boolean isR = ((tfQuestion) questionAdapter.getItem(position)).isRight();

                String answer = null;
                if (isR=true) {
                    answer = "true";
                } else if (isR=false) {
                    answer = "false";
                }
                long id_inList = questionAdapter.getId(position);
                bundle=new Bundle();
                tfFragment Fragment = new tfFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type", "tf");
                bundle.putString("Question", question);
                bundle.putString("answer", answer);
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
                    String ac=((numQuestion) questionAdapter.getItem(position)).getAccuracy();
                    long id_inList = questionAdapter.getId(position);
                    numFragment Fragment = new numFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("AnswerA", a1);
                    bundle.putString("AnswerB", a2);
                    bundle.putString("AnswerC", a3);
                    bundle.putString("AnswerD", a4);
                    bundle.putString("Question", q);
                    bundle.putString("correct", c);
                    bundle.putString("accuracy",ac);
                    bundle.putLong("LIST", id_inList);
                    bundle.putLong("ID", id);
                    bundle.putString("type","numeric");

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
                    String correct  = b.getString("correct");
                    updateForTablet(ans1, ans2, ans3, ans4, question, correct);
                } else if (b.getString("type").equals("tf")) {
                    String question = b.getString("Question");
                    String answer   = b.getString("answer");
                    updateForTF(question, answer);
                }else if (b.getString("type").equals("numeric")) {
                    String ans1 = b.getString("AnswerA");
                    String ans2 = b.getString("AnswerB");
                    String ans3 = b.getString("AnswerC");
                    String ans4 = b.getString("AnswerD");
                    String question = b.getString("Question");
                    String correct  = b.getString("correct");
                    String accuracy = b.getString("accuracy");
                    updateForNum(ans1, ans2, ans3, ans4, question, correct,accuracy);
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
        String acc=accuracy;

        Question q = new numQuestion(answer1, answer2, answer3, answer4, que, c, acc);
        questionArray.add(q);

        ContentValues cv = new ContentValues();
        cv.put(KEY_A, answer1);
        cv.put(KEY_B, answer2);
        cv.put(KEY_C, answer3);
        cv.put(KEY_D, answer4);
        cv.put(KEY_Question, que);
        cv.put(KEY_Correct, c);
        cv.put(KEY_Accuracy,acc);

        db.insert(table_name, "", cv);
        query = "SELECT * FROM " + table_name + ";";
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questionAdapter.notifyDataSetChanged();
    }

    public void updateForTF(String question,String answer) {
        String que = question;
        String ans = answer;
        Question q = new tfQuestion(ans=="true",que);
        questionArray.add(q);

        ContentValues cv = new ContentValues();
        cv.put(KEY_Question, que);
        cv.put(KEY_Correct, ans);

        db.insert(table_name,"",cv);
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
            View result;
            TextView Txt;
            result = CreateQuiz.this.getLayoutInflater().inflate(R.layout.question, null);
            Txt = result.findViewById(R.id.text_view);
            Txt.setText(getItem(position).getQuestion());
            return result;
        }

        @Override
        public long getItemId(int position) {
            Log.i("Why", "ARE WE HERE?!?!?!?!?!?!?!?!");
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

    public class QuizQuery extends AsyncTask<String, Integer, String> {
        private SQLiteDatabase db;
        private Boolean exist;
        private String A;
        private String B;
        private String C;
        private String D;
        private String correct;
        private String question;
        private InputStream stream;
        private String query;
        private Cursor cursor;
        int c;
        private String table_name = QuizDatabaseHelper.table_name;

        private ArrayList<Question> questionArray = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(10000);
                connection.connect();

                stream = connection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, null);
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:

                            break;


                        case XmlPullParser.TEXT:
                            break;


                        case XmlPullParser.END_TAG:
                    }
                    eventType=parser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            super.onProgressUpdate(value);
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
        }


    }
    }



