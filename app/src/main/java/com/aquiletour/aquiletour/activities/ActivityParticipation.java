package com.aquiletour.aquiletour.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

public class ActivityParticipation extends ActivityWithToolbar {
    public static final String ACTIVITY = "com.aquiletour.aquiletour.ACTIVITY";

    private Activity activity;
    private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.activity = (Activity) this.getIntent().getSerializableExtra(ACTIVITY);
        this.assignActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_participation;
    }

    private void assignActivity()
    {
        ((TextView) this.findViewById(R.id.activity_participation__activity_name)).setText(
            this.getResources().getString(R.string.activity_participation_activity_name, activity.getLabel())
        );

        this.participant = this.getCurrentParticipant();
        ((TextView) this.findViewById(R.id.activity_participation__participant_name)).setText(this.participant.getName());
    }

    private Participant getCurrentParticipant()
    {
        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this.getApplicationContext()));

        Participant lastParticipant = datasource.getLastParticipantFromActivity(this.activity);
        datasource.loadParticipants(this.activity);
        datasource.close();

        Log.d(this.getClass().toString(), "Participant trouvé? " + (lastParticipant == null ? "Non" : "Oui! " + lastParticipant.getName()));

        if (lastParticipant == null) {
            return this.activity.getParticipants().get(0);
        }

        int participantPosition = this.activity.getParticipants().indexOf(lastParticipant);

        Log.d(this.getClass().toString(), "Position du participant: " + String.valueOf(participantPosition));

        return this.activity.getParticipants().get(
            (this.activity.getParticipants().size() + participantPosition + 1) % this.activity.getParticipants().size()
        );
    }

    public void addParticipation(View view)
    {
        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this.getApplicationContext()));
        datasource.insertParticipation(this.activity, this.participant);

        this.assignActivity();
    }
}