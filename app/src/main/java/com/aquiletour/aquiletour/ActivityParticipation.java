package com.aquiletour.aquiletour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aquiletour.aquiletour.adapter.ActivityList;
import com.aquiletour.aquiletour.db.ActivityContract;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

import java.util.List;

public class ActivityParticipation extends AppCompatActivity {
    public static final String ACTIVITY = "com.aquiletour.aquiletour.ACTIVITY";

    private Activity activity;
    private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.activity = (Activity) this.getIntent().getSerializableExtra(ACTIVITY);
        this.assignActivity();
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
