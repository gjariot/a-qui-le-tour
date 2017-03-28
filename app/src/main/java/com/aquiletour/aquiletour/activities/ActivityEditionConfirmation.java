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

public class ActivityEditionConfirmation extends ActivityCreationConfirmation {
    private ActivityDataSource dataSource;

    protected void saveActivity()
    {
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String activityLabel = intent.getStringExtra(ActivityCreation.ACTIVITY_LABEL);
        this.activity = (Activity) intent.getSerializableExtra(ActivityParticipation.ACTIVITY);

        this.dataSource = new ActivityDataSource(new MySQLite(this));
        this.activity.setLabel(activityLabel.trim());

        this.dataSource.update(this.activity);

        List<Participant> participants = (List<Participant>) intent.getSerializableExtra(ActivityCreation.ACTIVITY_PARTICIPANTS);

        this.deleteRemovedParticipants(this.activity.getParticipants(), participants);
        this.updateParticipants(this.activity.getParticipants(), participants);
        this.insertParticipants(participants);

        this.activity.setParticipants(participants);

        ((TextView) this.findViewById(R.id.create_activity__confirmation_message)).setText(
                this.getResources().getString(R.string.edit_activity_confirmation_message, activity.getLabel())
        );
    }

    private void deleteRemovedParticipants(List<Participant> originalParticipants, List<Participant> currentParticipants) {
        for(Participant originalParticipant : originalParticipants) {
            Boolean participantFound = false;
            for (Participant currentParticipant : currentParticipants) {
                if (currentParticipant.getId() == originalParticipant.getId()) {
                    participantFound = true;
                }
            }

            if (participantFound == false) {
                this.dataSource.deleteParticipant(this.activity, originalParticipant);
            }
        }
    }

    private void updateParticipants(List<Participant> originalParticipants, List<Participant> currentParticipants) {
        for(Participant originalParticipant : originalParticipants) {
            for (Participant currentParticipant : currentParticipants) {
                if (currentParticipant.getId() == originalParticipant.getId()) {
                    if (
                        currentParticipant.getName().trim() != originalParticipant.getName().trim() ||
                        currentParticipant.getPicture() != originalParticipant.getPicture()
                    ) {
                        this.dataSource.update(currentParticipant);
                    }
                }
            }
        }
    }

    private void insertParticipants(List<Participant> currentParticipants) {
        for (Participant currentParticipant : currentParticipants) {
            if (currentParticipant.getId() < 1) {
                this.dataSource.insert(currentParticipant);
                this.dataSource.insertActivityParticipant(this.activity, currentParticipant);
            }
        }
    }
}
