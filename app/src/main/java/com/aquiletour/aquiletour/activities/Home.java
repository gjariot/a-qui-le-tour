package com.aquiletour.aquiletour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.adapter.ActivityList;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;

import java.util.List;

/**
 * Home of application: list activities
 */
public class Home extends ActivityWithToolbar {
    /**
     * Adapter used for the activites list view
     */
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

    /**
     * Launch activity to see clicked activity's details
     * @param view
     */
    public void showActivity(View view)
    {
        View parentView = (View) view.getParent();
        int position = (int) parentView.getTag(R.id.position);
        Activity activity = this.adapter.getItem(position);

        Intent intent = new Intent(this, ActivityParticipation.class);
        intent.putExtra(ActivityParticipation.ACTIVITY, activity);
        startActivity(intent);
    }

    /**
     * Load activities and assign them to the listView item
     */
    private void loadActivities() {
        ActivityDataSource dataSource = new ActivityDataSource(new MySQLite(this));
        List<Activity> activities = dataSource.getAll();
        dataSource.close();

        this.adapter = new ActivityList(activities, this);
        ListView listView = (ListView) this.findViewById(R.id.activities_list);
        listView.setAdapter(adapter);
    }

    /**
     * Display creation form after click on the add activity button
     */
    public void displayCreationForm() {
        this.startActivity(new Intent(this, ActivityCreation.class));
    }
}
