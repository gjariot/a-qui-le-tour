package com.aquiletour.aquiletour;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aquiletour.aquiletour.adapter.ActivityList;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ActivityList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);

        /*MySQLite db = new MySQLite(this);
        db.onUpgrade(db.getWritableDatabase(), 1, 1);*/

        this.loadActivities();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
