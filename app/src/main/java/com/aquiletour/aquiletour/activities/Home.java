package com.aquiletour.aquiletour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.aquiletour.aquiletour.activities.ActivityParticipation;
import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.adapter.ActivityList;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;

import java.util.List;


public class Home extends ActivityWithToolbar {
    public ActivityList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.loadActivities();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar__create_activity) {
            this.displayCreationForm();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showActivity(View view)
    {
        View parentView = (View) view.getParent();
        int position = (int) parentView.getTag(R.id.position);
        Activity activity = this.adapter.getItem(position);

        Intent intent = new Intent(this, ActivityParticipation.class);
        intent.putExtra(ActivityParticipation.ACTIVITY, activity);
        startActivity(intent);
    }

    private void loadActivities() {
        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this));
        List<Activity> activities = datasource.getAll();
        datasource.close();

        this.adapter = new ActivityList(activities, this);
        ListView listView = (ListView) this.findViewById(R.id.activities_list);
        listView.setAdapter(adapter);
    }

    public void displayCreationForm() {
        Intent intent = new Intent(this, ActivityCreation.class);
        startActivity(intent);
    }
}
