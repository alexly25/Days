package com.alex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13.01.14
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
public class SQLite extends SQLiteOpenHelper {

    private static final String LOG = "SQLite";

    public SQLite(Context context) {
        super(context, "Days", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, "onCreate() create tables");

        db.execSQL("create table " + SqlVariables.TABLE_TAGS + " ("
                + SqlVariables.COLUMN_TAG_ID + " integer not null primary key autoincrement,"
                + SqlVariables.COLUMN_TAG + " text not null unique" + ");");

        db.execSQL("create table " + SqlVariables.TABLE_NOTES + " ("
                + SqlVariables.COLUMN_NOTE_ID + " integer not null primary key autoincrement,"
                + SqlVariables.COLUMN_NOTE + " text not null unique" + ");");

        db.execSQL("create table " + SqlVariables.TABLE_TAG_NOTE + " ("
                + "id integer not null primary key autoincrement,"
                + SqlVariables.COLUMN_TAG_ID + " integer not null,"
                + SqlVariables.COLUMN_NOTE_ID + " integer not null,"
                + "FOREIGN KEY (" + SqlVariables.COLUMN_TAG_ID + ") REFERENCES " + SqlVariables.TABLE_TAGS + "(" + SqlVariables.COLUMN_TAG_ID + "),"
                + "FOREIGN KEY (" + SqlVariables.COLUMN_NOTE_ID + ") REFERENCES " + SqlVariables.TABLE_NOTES + "(" + SqlVariables.COLUMN_NOTE_ID + ")"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
