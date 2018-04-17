package com.example.chen.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class intake_form_patient_list_retrival extends Activity {

    static final String ACTIVITY_NAME ="PATIENT LIST";

    ProgressBar progressBar;
    TextView temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intake_form_patient_list_retrival);

        progressBar = (ProgressBar)findViewById(R.id.intakeForm_PatientListActivity_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        temp = (TextView)findViewById(R.id.intakeForm_PatientListActivity_Temp);

        new PatientQuery().execute();
    }

    public class PatientQuery extends AsyncTask<String, Integer,String>{
        private String tempStr;

        @Override
        protected String doInBackground(String...args){
            InputStream inputStream;

            try{
                String urlString = "http://torunski.ca/CST2335/PatientList.xml";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                inputStream = conn.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream,"UTF-8");

                int eventType = xpp.getEventType();

                //while you are not at the end of the document
                while(eventType!=XmlPullParser.END_DOCUMENT){
                    //Are you currently at a Start Tag？
                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            if(xpp.getName().equalsIgnoreCase("patient"))
                                tempStr+=xpp.getAttributeValue(null,"value");
                            break;
                        case XmlPullParser.TEXT:
                            tempStr+=xpp.getText();
                            break;
                        case XmlPullParser.END_TAG:
                    }
                    eventType=xpp.next();

                }


            }catch (FileNotFoundException fnfe){
                Log.e(ACTIVITY_NAME,fnfe.getMessage());
            }catch (MalformedURLException mfe){
                Log.e(ACTIVITY_NAME,mfe.getMessage());
            }catch (XmlPullParserException xppe){
                Log.e(ACTIVITY_NAME,xppe.getMessage());
            }catch(IOException ioe){
                Log.e(ACTIVITY_NAME, ioe.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer ... value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String args){
            temp.setText(tempStr);
        }


    }





}
