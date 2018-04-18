package com.example.chen.final_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Function extends AppCompatActivity {
    private QuizDatabaseHelper dbHelper = new QuizDatabaseHelper(this);
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_quiz_creater);
        db = dbHelper.getWritableDatabase();

        Button btn_import = (Button)findViewById(R.id.btn_import);
        Button btn_create = (Button)findViewById(R.id.btn_create);
        Button btn_stat   = (Button)findViewById(R.id.btn_stat);
        Button btn_quit   = (Button)findViewById(R.id.btn_quit);

        btn_import.setOnClickListener(e ->{
            QuizQuery QuizQuery=new QuizQuery();
            QuizQuery.execute();
            Toast.makeText(Function.this, R.string.Import, Toast.LENGTH_SHORT).show();
        });

        btn_create.setOnClickListener(v -> {
            Intent intent_c=new Intent(Function.this,CreateQuiz.class);
            startActivity(intent_c);
        });

        btn_stat.setOnClickListener(v -> {
            Intent intent_s=new Intent(Function.this,GetStat.class);
            startActivity(intent_s);
        });

        btn_quit.setOnClickListener(v -> {
            Intent intent_q=new Intent(Function.this,MainActivity.class);
            startActivity(intent_q);
        });
    }

    public class QuizQuery extends AsyncTask<String, Integer, String> {
        private String A;
        private String correct;
        private String question;
        private String answer;
        private String accuracy;
        private InputStream stream;
        private String query;
        private Cursor cursor;
        private String table_name = QuizDatabaseHelper.table_name;
        private ArrayList<Question> questionArray = new ArrayList<>();
        private multipleQuestion mQuestion=new multipleQuestion();

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://torunski.ca/CST2335/QuizInstance.xml");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(10000);
                connection.connect();

                stream = connection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(stream, null);
                int eventType = xpp.getEventType();
                int count=1;
                boolean b=false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("MultipleChoiceQuestion")) {
                                correct = xpp.getAttributeValue(null, "correct");
                                question = xpp.getAttributeValue(null, "question");
                                mQuestion.setQuestion(question);
                                mQuestion.setCorrect(correct);
                            }else if (xpp.getName().equals("Answer")) {
                                b=true;
                                if (correct.equals("1")) {
                                    correct = "1";
                                } else if (correct.equals("2")) {
                                    correct = "2";
                                } else if (correct.equals("3")) {
                                    correct = "3";
                                } else if (correct.equals("4")) {
                                    correct = "4";
                                }
                            }
                             else if (xpp.getName().equals("NumericQuestion")) {
                                answer = xpp.getAttributeValue(null, "answer");
                                question = xpp.getAttributeValue(null, "question");
                                accuracy=xpp.getAttributeValue(null,"accuracy");

                            } else if (xpp.getName().equals("TrueFalseQuestion")) {
                                question = xpp.getAttributeValue(null, "question");
                                correct = xpp.getAttributeValue(null, "answer");
                                A=xpp.getAttributeValue(null, "accuracy");

                            }
                            break;

                        case XmlPullParser.TEXT:
                            if(b==true){
                                if(count==1){
                                    mQuestion.setAnswerA("1");
                                } else if(count==2){
                                    mQuestion.setAnswerB("2");
                                } else if(count==3){
                                    mQuestion.setAnswerC("3");
                                } else if (count==4){
                                    mQuestion.setAnswerD("4");
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if(xpp.getName().equals("Answer")){
                                b=false;
                                count++;
                            }if (xpp.getName().equals("MultipleChoiceQuestion")){
                            questionArray.add(mQuestion);
                            ContentValues cv = new ContentValues();
                            cv.put(QuizDatabaseHelper.KEY_A, mQuestion.getAnswerA());
                            cv.put(QuizDatabaseHelper.KEY_B, mQuestion.getAnswerB());
                            cv.put(QuizDatabaseHelper.KEY_C, mQuestion.getAnswerC());
                            cv.put(QuizDatabaseHelper.KEY_D, mQuestion.getAnswerD());
                            cv.put(QuizDatabaseHelper.KEY_TYPE, "multipleChoice");
                            cv.put(QuizDatabaseHelper.KEY_Question, mQuestion.getQuestion());
                            cv.put(QuizDatabaseHelper.KEY_Correct, mQuestion.getCorrect());

                            db.insert(QuizDatabaseHelper.table_name, "", cv);
                            query="SELECT * FROM " + QuizDatabaseHelper.table_name + ";";
                            cursor = db.rawQuery(query, null);
                            cursor.moveToFirst();
                            count=1;
                        }else if (xpp.getName().equals("NumericQuestion")){
                            numQuestion numQuestion = new numQuestion(null,null,null,null,question,answer,accuracy);
                            questionArray.add(numQuestion);
                            ContentValues cv = new ContentValues();
                            cv.put(QuizDatabaseHelper.KEY_TYPE, "numeric");
                            cv.put(QuizDatabaseHelper.KEY_Question, question);
                            cv.put(QuizDatabaseHelper.KEY_Correct, answer);
                            cv.put(QuizDatabaseHelper.KEY_Accuracy, accuracy);

                            db.insert(QuizDatabaseHelper.table_name, "", cv);
                            query="SELECT * FROM " + QuizDatabaseHelper.table_name+ ";";
                            cursor = db.rawQuery(query, null);
                            cursor.moveToFirst();
                        }else if(xpp.getName().equals("TrueFalseQuestion")){
                            tfQuestion tfQuestion = new tfQuestion(correct.equals("true"),question);
                            questionArray.add(tfQuestion);
                            String result;
                            if (correct.equals("true")) {
                                result = "true";
                            } else {
                                result = "false";
                            }
                            ContentValues cv = new ContentValues();
                            cv.put(QuizDatabaseHelper.KEY_TYPE, "tf");
                            cv.put(QuizDatabaseHelper.KEY_Question, question);
                            cv.put(QuizDatabaseHelper.KEY_Correct, result);

                            db.insert(QuizDatabaseHelper.table_name, "", cv);
                            cursor = db.rawQuery("SELECT * FROM " + QuizDatabaseHelper.table_name + ";", null);
                            cursor.moveToFirst();

                        }
                            break;
                        default:
                            break;
                    }
                    eventType = xpp.next();
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
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
//            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
