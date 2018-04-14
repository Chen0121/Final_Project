package com.example.chen.final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zeyan on 2018-04-12.
 */

public class PatientDatabaseHelper extends SQLiteOpenHelper {

    private static PatientDatabaseHelper sInstance;

    public static final String DATABASE_NAME = "PatientsDatabase.db";
    public static final int VERSION_NUMBER =1 ;
    public static final String TABLE_NAME = "PatientTable";
    public static final String COLUMN_ID = "COL_ID";
    public static final String COLUMN_NAME = "COL_NAME";
    public static final String COLUMN_ADDRESS = "COL_ADDRESS";
    public static final String COLUMN_BIRTHDAY = "COL_BIRTHDAY";
    public static final String COLUMN_PHONE = "COL_PHONE";
    public static final String COLUMN_HEALTHCARD = "COL_HEALTHCARD";
    public static final String COLUMN_DESCRIPTION = "COL_DESCRIPTION";
    public static final String COLUMN_PREVIOUSSURGERY = "COL_PREVIOUSSURGERY";
    public static final String COLUMN_ALLERGIES = "COL_ALLERGIES";
    public static final String COLUMN_GLASSESBOUGHT = "COL_GLASSESBOUGHT";
    public static final String COLUMN_GLASSESSTORE = "COL_GLASSESSTORE";
    public static final String COLUMN_BENEFITS = "COL_BENEFIT";
    public static final String COLUMN_HADBRACES = "COL_HADBRACES";

    public PatientDatabaseHelper(Context ctx){

        super(ctx,DATABASE_NAME,null,VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_ADDRESS + " TEXT, " +
                        COLUMN_BIRTHDAY + " TEXT, " +
                        COLUMN_PHONE + " TEXT, " +
                        COLUMN_HEALTHCARD + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_PREVIOUSSURGERY + " TEXT, " +
                        COLUMN_ALLERGIES + " TEXT, " +
                        COLUMN_GLASSESBOUGHT + " TEXT, " +
                        COLUMN_GLASSESSTORE + " TEXT, " +
                        COLUMN_BENEFITS + " TEXT, " +
                        COLUMN_HADBRACES + " TEXT);"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);//delete any existing data.
        onCreate(db);//make a new database

        Log.i("PatientDatabaseHelper","Calling onUpdate, oldVersion="+oldVersion+"newVersion="+newVersion);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void onOPen(SQLiteDatabase db){
        Log.i("PatientDatabase","Database Opened");
    }


    public static synchronized PatientDatabaseHelper getInstance(Context ctx){
        if (sInstance == null) {
            sInstance = new PatientDatabaseHelper(ctx.getApplicationContext());
        }
        return sInstance;
    }

}
