package com.aquiletour.aquiletour.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

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

    public Participant insert(Participant participant) {
        ContentValues values = new ContentValues();
        values.put(ActivityContract.Participant.COLUMN_NAME_NAME, participant.getName());

        long id = this.db.getWritableDatabase().insert(ActivityContract.Participant.TABLE_NAME, null, values);

        participant.setId(id);
        return participant;
    }

    public void insertActivityParticipant(Activity activity, Participant participant) {
        ContentValues values = new ContentValues();
        values.put(ActivityContract.ActivityParticipants.COLUMN_NAME_PARTICIPANT, participant.getId());
        values.put(ActivityContract.ActivityParticipants.COLUMN_NAME_ACTIVITY, activity.getId());

        this.db.getWritableDatabase().insert(ActivityContract.ActivityParticipants.TABLE_NAME, null, values);
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

    public void delete(Activity activity) {
        String[] clause = {String.valueOf(activity.getId())};
        this.db.getWritableDatabase().delete(ActivityContract.ActivityParticipants.TABLE_NAME, ActivityContract.ActivityParticipants.COLUMN_NAME_ACTIVITY + " = ?", clause);
        this.db.getWritableDatabase().delete(ActivityContract.Activity.TABLE_NAME, ActivityContract.Activity._ID + " = ?", clause);
    }
}
