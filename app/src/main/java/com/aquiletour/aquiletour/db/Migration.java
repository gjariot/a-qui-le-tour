package com.aquiletour.aquiletour.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Method;

public class Migration {
    private int oldVersion;

    private int newVersion;

    private SQLiteDatabase db;

    public Migration(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.db = db;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public void runMigrationScripts()
    {
        this.updateFrom1To2();
    }

    public void updateFrom1To2()
    {
        this.db.execSQL("create table " + ActivityContract.ActivityParticipation.TABLE_NAME + "(" +
                ActivityContract.ActivityParticipation._ID + " integer primary key autoincrement, " +
                ActivityContract.ActivityParticipation.COLUMN_NAME_ACTIVITY + " integer not null, " +
                ActivityContract.ActivityParticipation.COLUMN_NAME_PARTICIPANT + " integer not null, " +
                ActivityContract.ActivityParticipation.COLUMN_NAME_DATE + " integer not null);");
    }
}