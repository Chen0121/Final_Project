package com.example.chen.final_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//public class ImportQuiz extends Activity {

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        db = dbHelper.getWritableDatabase();
//        QuizQuery quizquery = new QuizQuery();
//        quizquery.execute();
//    }

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
//}
