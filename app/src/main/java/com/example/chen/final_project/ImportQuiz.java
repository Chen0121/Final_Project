package com.example.chen.final_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImportQuiz extends Activity {
    private SQLiteDatabase db;
    private Boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuizDatabaseHelper dbHelper = new QuizDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        QuizQuery quizquery = new QuizQuery();
        quizquery.execute();
    }


    public class QuizQuery extends AsyncTask<String, Integer, String> {
        private String A;
        private String B;
        private String C;
        private String D;
        private String correct;
        private String question;
        private InputStream stream;
        private Cursor cursor;
        private String table_numeric = QuizDatabaseHelper.table_numeric;
        private String table_tf = QuizDatabaseHelper.table_tf;
        private String table_multiple = QuizDatabaseHelper.table_multiple;

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
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(stream, null);
                int eventType=parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equalsIgnoreCase("MultipleChoiceQuestion")) {
                                question = parser.getAttributeValue(null, "question");
                                correct = parser.getAttributeValue(null, "correct");
                                if(correct.equals("1")){
                                    correct = "A";
                                }else if(correct.equals("2")){
                                    correct = "B";
                                }else if(correct.equals("3")){
                                    correct = "C";
                                }else if(correct.equals("4")) {
                                    correct = "D";
                                }else{
                                    throw new IllegalArgumentException("answer should be A/B/C/D");
                                }

                            }else if(parser.getName().equalsIgnoreCase("TrueFalseQuestion")){
                                question = parser.getAttributeValue(null, "question");
                                correct = parser.getAttributeValue(null, "answer");

                                cursor = db.rawQuery("SELECT * FROM " + table_tf + ";", null);
                                cursor.moveToFirst();
                                while(!cursor.isAfterLast()){
                                    String db_question = cursor.getString( cursor.getColumnIndex( QuizDatabaseHelper.KEY_Question ) );
                                    if(question.equals(db_question)){
                                        exist = true;
                                        break;
                                    }
                                    cursor.moveToNext();
                                }
                                if(!exist){
                                    db.execSQL("INSERT INTO " + table_tf + " ( " + QuizDatabaseHelper.KEY_Question
                                            + " , " + QuizDatabaseHelper.KEY_Correct + " ) VALUES ( '" + question + "' , '" + correct + "' );");
                                }else{
                                    Toast.makeText(ImportQuiz.this, "tf already exist", Toast.LENGTH_SHORT).show();
                                    exist = false;
                                }
                            }else if (parser.getName().equalsIgnoreCase("NumericQuestion")){
                                question = parser.getAttributeValue(null, "question");
                                A = parser.getAttributeValue(null, "accuracy");
                                correct = parser.getAttributeValue(null, "answer");
                                cursor = db.rawQuery("SELECT * FROM " + table_numeric + ";",null);
                                cursor.moveToFirst();
                                while(!cursor.isAfterLast()){
                                    String db_question = cursor.getString( cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question ) );
                                    if(question.equals(db_question)){
                                        exist = true;
                                        break;
                                    }
                                    cursor.moveToNext();
                                }
                                if(!exist){
                                    db.execSQL("INSERT INTO " + table_numeric + " ( " + QuizDatabaseHelper.KEY_Question
                                            + " , " + QuizDatabaseHelper.KEY_A + " , " + QuizDatabaseHelper.KEY_Correct + " ) VALUES ( '"
                                            + question + "' , '" + A + "' , '" + correct + "' );");
                                }else{
                                    Toast.makeText(ImportQuiz.this, "numeric already exist", Toast.LENGTH_SHORT).show();
                                    exist = false;
                                }
                            }
                            break;

                        case XmlPullParser.TEXT:
                            if(parser.getText().matches("^[\\w.-]+$")) {
                                if (A == null) {
                                    A = parser.getText();
                                    break;
                                } else if (B == null) {
                                    B = parser.getText();
                                    break;
                                } else if (C == null) {
                                    C = parser.getText();
                                    break;
                                } else if (D == null) {
                                    D = parser.getText();
                                    cursor = db.rawQuery("SELECT * FROM " + table_multiple + ";", null);
                                    cursor.moveToFirst();
                                    while (!cursor.isAfterLast()) {
                                        String db_question = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.KEY_Question));
                                        if (question.equals(db_question)) {
                                            exist = true;
                                            break;
                                        }
                                        cursor.moveToNext();
                                    }
                                    if (!exist) {
                                        db.execSQL("INSERT INTO " + table_multiple + " ( " + QuizDatabaseHelper.KEY_Question + " , " + QuizDatabaseHelper.KEY_A
                                                + " , " + QuizDatabaseHelper.KEY_B + " , " + QuizDatabaseHelper.KEY_C + " , " + QuizDatabaseHelper.KEY_D
                                                + " , " + QuizDatabaseHelper.KEY_Correct + " ) VALUES ( '" + question + "' , '" + A + "' , '"
                                                + B + "' , '" + C + "' , '" + D + "' , '" + correct + "' );");
                                    } else {
                                        Toast.makeText(ImportQuiz.this, "multiple choice exist", Toast.LENGTH_SHORT).show();
                                        exist = false;
                                    }
                                    A = null;
                                    B = null;
                                    C = null;
                                    D = null;
                                }
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;


                    }
                    eventType = parser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(stream != null ) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...value){
            super.onProgressUpdate(value);
        }

        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);
            Intent intent=new Intent(ImportQuiz.this,CreateQuiz.class);
            startActivity(intent);
        }
    }





}
