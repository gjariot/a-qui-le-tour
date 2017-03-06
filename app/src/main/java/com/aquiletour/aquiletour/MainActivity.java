package com.aquiletour.aquiletour;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadActivities();
        this.loadActions();
    }

    private void loadActivities() {
        ActivityDataSource datasource = new ActivityDataSource(new MySQLite(this));
        List<Activity> activities = datasource.getAll();

        ArrayAdapter<Activity> adapter = new ArrayAdapter<Activity>(this, R.layout.activities_list_item, activities);

        ListView listView = (ListView) this.findViewById(R.id.activities_list);
        listView.setAdapter(adapter);
    }

    private void loadActions() {
        ArrayList<String> actions = new ArrayList<String>();
        actions.add("Créer une activité");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activities_list_item, actions);
        ListView listView = (ListView) this.findViewById(R.id.actions_list);
        listView.setAdapter(adapter);

        // Create a message handling object as an anonymous class.
        AdapterView.OnItemClickListener clickHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)  {
                MainActivity.this.displayCreationForm();
            }
        };

        listView.setOnItemClickListener(clickHandler);
    }

    public void displayCreationForm() {
        Intent intent = new Intent(this, ActivityCreation.class);
        startActivity(intent);
    }
}
