package com.aquiletour.aquiletour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.aquiletour.aquiletour.adapter.ActivityCreationParticipantsList;

import java.util.ArrayList;

public class ActivityCreation extends AppCompatActivity {
    public static final String ACTIVITY_LABEL = "com.aquiletour.aquiletour.ACTIVITY_LABEL";
    public static final String ACTIVITY_PARTICIPANTS = "com.aquiletour.aquiletour.ACTIVITY_PARTICIPANTS";

    private ActivityCreationParticipantsList participantsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView participants = (ListView) this.findViewById(R.id.create_activity___participants_list);
        this.participantsListAdapter = new ActivityCreationParticipantsList(new ArrayList<String>(), this);
        participants.setAdapter(this.participantsListAdapter);
    }

    public void submitCreationForm(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.activity_label);
        String activityLabel = editText.getText().toString();
        intent.putExtra(ACTIVITY_LABEL, activityLabel);

        String[] participants = new String[this.participantsListAdapter.participants.size()];
        for (int index = 0; index < this.participantsListAdapter.getCount(); index++) {
            participants[index] = this.participantsListAdapter.getItem(index);
        }

        intent.putExtra(ACTIVITY_PARTICIPANTS, participants);

        startActivity(intent);
    }

    public void addParticipant(View view) {
        EditText participantAdd = (EditText) this.findViewById(R.id.create_activity__participant_name);
        Log.d("ActivityCreation", "Ajout de " + participantAdd.getText().toString());
        this.participantsListAdapter.participants.add(participantAdd.getText().toString());
        this.participantsListAdapter.notifyDataSetChanged();
        participantAdd.setText("");
    }
}
