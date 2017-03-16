package com.aquiletour.aquiletour.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLite extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "aquiletour.db";

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        this.getDatabaseCreationQuery(database);
    }

    private void getDatabaseCreationQuery(SQLiteDatabase database) {
        database.execSQL("create table " + ActivityContract.Activity.TABLE_NAME + "(" +
                ActivityContract.Activity._ID + " integer primary key autoincrement, " +
                ActivityContract.Activity.COLUMN_NAME_LABEL + " text not null);");

        database.execSQL("create table " + ActivityContract.Participant.TABLE_NAME + "(" +
                ActivityContract.Participant._ID + " integer primary key autoincrement, " +
                ActivityContract.Participant.COLUMN_NAME_NAME + " text not null);");

        database.execSQL("create table " + ActivityContract.ActivityParticipants.TABLE_NAME + "(" +
                ActivityContract.ActivityParticipants._ID + " integer primary key autoincrement, " +
                ActivityContract.ActivityParticipants.COLUMN_NAME_ACTIVITY + " integer not null, " +
                ActivityContract.ActivityParticipants.COLUMN_NAME_PARTICIPANT + " integer not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ActivityContract.Activity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActivityContract.Participant.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ActivityContract.ActivityParticipants.TABLE_NAME);
        onCreate(db);
    }

}