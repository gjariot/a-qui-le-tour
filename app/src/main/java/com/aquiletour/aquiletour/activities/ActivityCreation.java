package com.aquiletour.aquiletour.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.adapter.ActivityCreationParticipantsList;
import com.aquiletour.aquiletour.entity.Participant;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActivityCreation extends ActivityWithToolbar {
    public static final String ACTIVITY_LABEL = "com.aquiletour.aquiletour.ACTIVITY_LABEL";
    public static final String ACTIVITY_PARTICIPANTS = "com.aquiletour.aquiletour.ACTIVITY_PARTICIPANTS";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    protected ActivityCreationParticipantsList participantsListAdapter;

    static String participantPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        List<Participant> participantsList;

        if (savedInstanceState != null) {
            participantsList = (List<Participant>) savedInstanceState.getSerializable("participants");
        } else {
            participantsList = new ArrayList<Participant>();
        }

        ListView participants = (ListView) this.findViewById(R.id.create_activity___participants_list);
        this.participantsListAdapter = new ActivityCreationParticipantsList(participantsList, this);
        participants.setAdapter(this.participantsListAdapter);

        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            this.findViewById(R.id.create_activity___add_picture_button).setVisibility(View.GONE);
        }

        this.findViewById(R.id.create_activity__participant_name).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_creation;
    }

    public void submitCreationForm(View view) {
        Intent intent = new Intent(this, ActivityCreationConfirmation.class);
        EditText editText = (EditText) findViewById(R.id.activity_label);
        String activityLabel = editText.getText().toString();

        intent.putExtra(ACTIVITY_LABEL, activityLabel);
        intent.putExtra(ACTIVITY_PARTICIPANTS, (Serializable) this.participantsListAdapter.getParticipants());

        startActivity(intent);
    }

    public void addParticipant(View view) {
        EditText participantAdd = (EditText) this.findViewById(R.id.create_activity__participant_name);

        Participant participant = new Participant();
        participant.setName(participantAdd.getText().toString().trim()).setPicture(ActivityCreation.participantPicture);

        this.participantsListAdapter.getParticipants().add(participant);
        this.participantsListAdapter.notifyDataSetChanged();

        participantAdd.setText("");
        this.findViewById(R.id.create_activity___participant_picture).setVisibility(View.INVISIBLE);
    }

    public void addPicture(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePicture.resolveActivity(this.getPackageManager()) != null) {
            File picture;

            try {
                picture = this.getPicture();
            } catch (IOException ex) {
                this.findViewById(R.id.create_activity___add_picture_button).setVisibility(View.GONE);
                return;
            }

            Uri pictureURI = FileProvider.getUriForFile(this, "com.aquiletour.aquiletour.fileprovider", picture);

            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, pictureURI);

            this.startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("ActivityCreation", "onActivityResult(): " + ActivityCreation.participantPicture);
            ImageView thumbnail = (ImageView) this.findViewById(R.id.create_activity___participant_picture);
            thumbnail.setImageBitmap(BitmapFactory.decodeFile(ActivityCreation.participantPicture));
            thumbnail.setRotation(90);
            thumbnail.setVisibility(View.VISIBLE);
        }
    }

    private File getPicture() throws IOException {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                "picture_" + UUID.randomUUID().toString(),
                ".jpg",
                storageDir
        );

        ActivityCreation.participantPicture = image.getAbsolutePath();

        return image;
    }

    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);

        savedState.putSerializable("participants", (Serializable) this.participantsListAdapter.getParticipants());
    }
}
