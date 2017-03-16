package com.aquiletour.aquiletour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.saveActivity();
    }

    private void saveActivity()
    {
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(ActivityCreation.ACTIVITY_LABEL);

        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this));
        Activity activity = new Activity();
        activity.setLabel(message);

        datasource.insert(activity);

        String[] participants = intent.getStringArrayExtra(ActivityCreation.ACTIVITY_PARTICIPANTS);
        for (int index = 0; index < participants.length; index++) {
            Participant participant = new Participant();
            participant.setName(participants[index]);

            datasource.insert(participant);
            datasource.insertActivityParticipant(activity, participant);
        }

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("L'activité " + message + " a bien été enregistrée.");
    }
}
