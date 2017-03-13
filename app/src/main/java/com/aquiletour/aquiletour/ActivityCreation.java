package com.aquiletour.aquiletour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class ActivityCreation extends AppCompatActivity {
    public static final String ACTIVITY_LABEL = "com.aquiletour.aquiletour.ACTIVITY_LABEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submitCreationForm(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String activityLabel = editText.getText().toString();
        intent.putExtra(ACTIVITY_LABEL, activityLabel);

        startActivity(intent);
    }
}
