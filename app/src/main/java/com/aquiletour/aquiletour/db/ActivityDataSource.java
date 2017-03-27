package com.aquiletour.aquiletour.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

import java.sql.Timestamp;
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

    public Activity update(Activity activity) {
        ContentValues values = new ContentValues();
        values.put(ActivityContract.Activity.COLUMN_NAME_LABEL, activity.getLabel());

        String[] clause = {String.valueOf(activity.getId())};

        this.db.getWritableDatabase().update(ActivityContract.Activity.TABLE_NAME, values, ActivityContract.Activity._ID + " = ?", clause);

        return activity;
    }

    public Participant update(Participant participant) {
        ContentValues values = new ContentValues();
        values.put(ActivityContract.Participant.COLUMN_NAME_NAME, participant.getName());
        values.put(ActivityContract.Participant.COLUMN_NAME_PICTURE, participant.getPicture());

        String[] clause = {String.valueOf(participant.getId())};

        this.db.getWritableDatabase().update(ActivityContract.Participant.TABLE_NAME, values, ActivityContract.Participant._ID + " = ?", clause);

        return participant;
    }

    public Participant insert(Participant participant) {
        ContentValues values = new ContentValues();
        values.put(ActivityContract.Participant.COLUMN_NAME_NAME, participant.getName());
        values.put(ActivityContract.Participant.COLUMN_NAME_PICTURE, participant.getPicture());

        long id = this.db.getWritableDatabase().insert(ActivityContract.Participant.TABLE_NAME, null, values);

        participant.setId(id);
        return participant;
    }

    public Participant getLastParticipantFromActivity(Activity activity) {
        String[] clause = {String.valueOf(activity.getId())};
        Cursor cursor = this.db.getReadableDatabase().rawQuery("SELECT pa._id, pa.name, pa.picture FROM participant pa INNER JOIN activity_participation pc ON (pa._id = pc.participant_id) WHERE activity_id = ? ORDER BY date DESC LIMIT 1", clause);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();

        Participant participant = this.mapCursorToParticipant(cursor);

        cursor.close();
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

    private Participant mapCursorToParticipant(Cursor entry) {
        Participant participant = new Participant();
        return participant
                .setId(entry.getLong(0))
                .setName(entry.getString(1))
                .setPicture(entry.getString(2));
    }

    public void delete(Activity activity) {
        String[] clause = {String.valueOf(activity.getId())};
        this.db.getWritableDatabase().delete(ActivityContract.ActivityParticipants.TABLE_NAME, ActivityContract.ActivityParticipants.COLUMN_NAME_ACTIVITY + " = ?", clause);
        this.db.getWritableDatabase().delete(ActivityContract.Activity.TABLE_NAME, ActivityContract.Activity._ID + " = ?", clause);
    }

    public void deleteParticipant(Activity activity, Participant participant) {
        String[] clause = {String.valueOf(activity.getId()), String.valueOf(participant.getId())};
        this.db.getWritableDatabase().delete(
            ActivityContract.ActivityParticipants.TABLE_NAME,
            ActivityContract.ActivityParticipants.COLUMN_NAME_ACTIVITY + " = ? AND " + ActivityContract.ActivityParticipants.COLUMN_NAME_PARTICIPANT + " = ?",
            clause
        );
    }

    public void insertParticipation(Activity activity, Participant participant) {
        Long timestamp = System.currentTimeMillis() / 1000;

        ContentValues values = new ContentValues();
        values.put(ActivityContract.ActivityParticipation.COLUMN_NAME_PARTICIPANT, participant.getId());
        values.put(ActivityContract.ActivityParticipation.COLUMN_NAME_ACTIVITY, activity.getId());
        values.put(ActivityContract.ActivityParticipation.COLUMN_NAME_DATE, timestamp);

        this.db.getWritableDatabase().insert(ActivityContract.ActivityParticipation.TABLE_NAME, null, values);
    }

    public void loadParticipants(Activity activity) {
        activity.setParticipants(this.getActivityParticipants(activity));
    }

    private List<Participant> getActivityParticipants(Activity activity) {
        List<Participant> participants = new ArrayList<Participant>();

        String[] clause = {String.valueOf(activity.getId())};
        Cursor cursor = this.db.getReadableDatabase().rawQuery("SELECT p._id, p.name, p.picture FROM participant p INNER JOIN activity_participant ap ON (p._id = ap.participant_id) WHERE activity_id = ?", clause);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Participant participant = this.mapCursorToParticipant(cursor);
            participants.add(participant);
            cursor.moveToNext();
        }

        cursor.close();
        return participants;
    }

    public void close() {
        this.db.close();
    }
}
