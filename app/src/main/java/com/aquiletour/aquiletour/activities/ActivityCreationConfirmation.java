package com.aquiletour.aquiletour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

public class ActivityCreationConfirmation extends ActivityWithToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.saveActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_display_message;
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
