package com.example.chen.final_project;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuizDatabaseHelper extends SQLiteOpenHelper {
    private static final int db_version = 7;
    private static final String db_name = "Quiz.db";
    public static final String table_multiple = "table_multipleChoice";
    public static final String table_numeric = "table_numericQuestion";
    public static final String table_tf = "table_tfQuestion";
    public static final String KEY_ID = "id";
    public static final String KEY_Question = "question";
    public static final String KEY_Correct = "correctAnswer";
    public static final String KEY_A = "AnswerA";
    public static final String KEY_B = "AnswerB";
    public static final String KEY_C = "AnswerC";
    public static final String KEY_D = "AnswerD";


    QuizDatabaseHelper(Context ctx) {
        super(ctx, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.i("QuizDatabaseHelper", "onCreate");
            db.execSQL("CREATE TABLE " + table_multiple + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_Question + " text, " + KEY_Correct + " text, " + KEY_A + " text, " + KEY_B + " text, "
                    + KEY_C + " text, " + KEY_D+ " text);");

            /*db.execSQL( "CREATE TABLE " + table_numeric + " ( "+ KEY_ID +" INTEGER PRIMARY KEY " +
                    "AUTOINCREMENT, " +KEY_Question+ "text, "+KEY_Correct +"text, "+ KEY_A
                    + "text )"
            );
            db.execSQL( "CREATE TABLE " + table_tf + " ( "+ KEY_ID +" INTEGER PRIMARY KEY " +
                    "AUTOINCREMENT, " +KEY_Question+ "text, "+KEY_Correct +"text )"
            );*/
        } catch (SQLException e) {
            Log.e("QuizDatabaseHelper", e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + table_multiple + ";");
        db.execSQL("DROP TABLE IF EXISTS " + table_numeric + ";");
        db.execSQL("DROP TABLE IF EXISTS " + table_tf + ";");
        onCreate(db);
        Log.i("QuizDatabaseHelper", "onUpgrade, oldVer=" + oldVer + " newVer= " + newVer);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + table_multiple + ";");
        db.execSQL("DROP TABLE IF EXISTS " + table_numeric + ";");
        db.execSQL("DROP TABLE IF EXISTS " + table_tf + ";");
        onCreate(db);
        Log.i("QuizDatabaseHelper", "onDowngrade, oldVer=" + oldVer + " newVer= " + newVer);
    }

}

