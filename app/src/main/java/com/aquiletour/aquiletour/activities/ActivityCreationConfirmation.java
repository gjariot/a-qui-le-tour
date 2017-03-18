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
        String activityLabel = intent.getStringExtra(ActivityCreation.ACTIVITY_LABEL);

        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this));
        Activity activity = new Activity();
        activity.setLabel(activityLabel);

        datasource.insert(activity);

        String[] participants = intent.getStringArrayExtra(ActivityCreation.ACTIVITY_PARTICIPANTS);
        for (int index = 0; index < participants.length; index++) {
            Participant participant = new Participant();
            participant.setName(participants[index]);

            datasource.insert(participant);
            datasource.insertActivityParticipant(activity, participant);
        }

        ((TextView) this.findViewById(R.id.create_activity__confirmation_message)).setText(
            this.getResources().getString(R.string.create_activity_confirmation_message, activity.getLabel())
        );
    }
}
