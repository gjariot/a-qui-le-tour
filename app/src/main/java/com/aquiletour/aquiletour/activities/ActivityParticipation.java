package com.aquiletour.aquiletour.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;
import com.aquiletour.aquiletour.entity.Participant;

import java.io.File;

public class ActivityParticipation extends ActivityWithToolbar {
    public static final String ACTIVITY = "com.aquiletour.aquiletour.ACTIVITY";

    private Activity activity;
    private Participant participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.activity = (Activity) this.getIntent().getSerializableExtra(ACTIVITY);
        this.assignActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edition, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_participation;
    }

    private void assignActivity()
    {
        ((TextView) this.findViewById(R.id.activity_participation__activity_name)).setText(
            this.getResources().getString(R.string.activity_participation_activity_name, activity.getLabel())
        );

        this.participant = this.getCurrentParticipant();
        ((TextView) this.findViewById(R.id.activity_participation__participant_name)).setText(this.participant.getName());

        if (this.participant.getPicture() != null) {
            File picture = new File(this.participant.getPicture());
            ImageView participantPicture = (ImageView) this.findViewById(R.id.activity_participation__participant_picture);

            Point screensize = new Point();
            this.getWindowManager().getDefaultDisplay().getSize(screensize);
            int expectedWidth = screensize.x;
            Bitmap pictureBmp = BitmapFactory.decodeFile(picture.getAbsolutePath());
            int expectedHeight = (int) (pictureBmp.getWidth() * expectedWidth / pictureBmp.getHeight());

            participantPicture.setImageBitmap(Bitmap.createScaledBitmap(pictureBmp, expectedHeight, expectedWidth, true));
            participantPicture.setRotation(90);
        } else {
            this.findViewById(R.id.activity_participation__participant_picture).setVisibility(View.INVISIBLE);
        }
    }

    private Participant getCurrentParticipant()
    {
        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this.getApplicationContext()));

        Participant lastParticipant = datasource.getLastParticipantFromActivity(this.activity);
        datasource.loadParticipants(this.activity);
        datasource.close();

        if (lastParticipant == null) {
            return this.activity.getParticipants().get(0);
        }

        int participantPosition = this.activity.getParticipants().indexOf(lastParticipant);

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

    /**
     * Display edition form after click on the add activity button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar__edit_activity) {
            Intent intent = new Intent(this, ActivityEdition.class);
            intent.putExtra(ACTIVITY, this.activity);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
