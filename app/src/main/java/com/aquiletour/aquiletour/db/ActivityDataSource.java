package com.aquiletour.aquiletour.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.aquiletour.aquiletour.entity.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityDataSource {
    private MySQLite db;

    public ActivityDataSource(MySQLite db) {
        this.db = db;
    }

    public Activity insert(Activity activity) {
        ContentValues values = new ContentValues();
        values.put(ActivityContract.Activity.COLUMN_NAME_LABEL, activity.getLabel());

        long id = this.db.getWritableDatabase().insert(ActivityContract.Activity.TABLE_NAME, null, values);

        activity.setId(id);
        return activity;
    }

    public List<Activity> getAll() {
        List<Activity> activities = new ArrayList<Activity>();

        String[] columns = {ActivityContract.Activity._ID, ActivityContract.Activity.COLUMN_NAME_LABEL};

        Cursor cursor = this.db.getReadableDatabase().query(
            ActivityContract.Activity.TABLE_NAME,
            columns,
            null, null, null, null, null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Activity activity = this.mapCursorToActivity(cursor);
            activities.add(activity);
            cursor.moveToNext();
        }

        cursor.close();
        return activities;
    }

    private Activity mapCursorToActivity(Cursor entry) {
        Activity activity = new Activity();
        return activity
            .setId(entry.getLong(0))
            .setLabel(entry.getString(1));
    }
}
