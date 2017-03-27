package com.aquiletour.aquiletour.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.adapter.ActivityCreationParticipantsList;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityEdition extends ActivityCreation {
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        List<Participant> participantsList;

        this.activity = (Activity) this.getIntent().getSerializableExtra(ActivityParticipation.ACTIVITY);

        if (savedInstanceState != null) {
            participantsList = (List<Participant>) savedInstanceState.getSerializable("participants");
        } else {
            participantsList = this.activity.getParticipants();
        }

        ListView participants = (ListView) this.findViewById(R.id.create_activity___participants_list);
        this.participantsListAdapter = new ActivityCreationParticipantsList(participantsList, this);
        participants.setAdapter(this.participantsListAdapter);

        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            this.findViewById(R.id.create_activity___add_picture_button).setVisibility(View.GONE);
        }

        ((EditText) this.findViewById(R.id.activity_label)).setText(this.activity.getLabel());
        Button submitButton = (Button) this.findViewById(R.id.button);
        submitButton.setText(R.string.edit_activity_submit_button);

        final ActivityEdition currentActivity = this;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentActivity.submitEditionForm(view);
            }
        });
    }

    public void submitEditionForm(View view) {
        Intent intent = new Intent(this, ActivityEditionConfirmation.class);

        intent.putExtra(ActivityParticipation.ACTIVITY, this.activity);
        intent.putExtra(ACTIVITY_LABEL, ((EditText) this.findViewById(R.id.activity_label)).getText().toString());
        intent.putExtra(ACTIVITY_PARTICIPANTS, (Serializable) this.participantsListAdapter.getParticipants());

        this.startActivity(intent);
    }
}
