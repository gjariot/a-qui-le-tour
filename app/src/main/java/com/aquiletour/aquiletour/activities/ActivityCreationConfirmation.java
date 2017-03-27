package com.aquiletour.aquiletour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

import java.util.List;

public class ActivityCreationConfirmation extends ActivityWithToolbar {
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.saveActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_display_message;
    }

    protected void saveActivity()
    {
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String activityLabel = intent.getStringExtra(ActivityCreation.ACTIVITY_LABEL);

        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this));
        Activity activity = new Activity();
        activity.setLabel(activityLabel);

        datasource.insert(activity);

        List<Participant> participants = (List<Participant>) intent.getSerializableExtra(ActivityCreation.ACTIVITY_PARTICIPANTS);

        activity.setParticipants(participants);

        for (int index = 0; index < participants.size(); index++) {
            Participant participant = participants.get(index);

            datasource.insert(participant);
            datasource.insertActivityParticipant(activity, participant);
        }

        ((TextView) this.findViewById(R.id.create_activity__confirmation_message)).setText(
            this.getResources().getString(R.string.create_activity_confirmation_message, activity.getLabel())
        );

        this.activity = activity;
    }

    /**
     * Launch activity to see activity's details
     * @param view
     */
    public void showActivity(View view)
    {
        Intent intent = new Intent(this, ActivityParticipation.class);
        intent.putExtra(ActivityParticipation.ACTIVITY, this.activity = activity);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
    }
}
