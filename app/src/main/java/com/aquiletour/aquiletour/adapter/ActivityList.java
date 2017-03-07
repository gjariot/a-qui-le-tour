package com.aquiletour.aquiletour.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aquiletour.aquiletour.MainActivity;
import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.db.ActivityDataSource;
import com.aquiletour.aquiletour.db.MySQLite;
import com.aquiletour.aquiletour.entity.Activity;

import java.util.List;

public class ActivityList extends BaseAdapter {
    private List<Activity> activities;
    private Context context;

    public ActivityList(List<Activity> activities, Context context) {
        this.activities = activities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.activities.size();
    }

    @Override
    public Activity getItem(int position) {
        return this.activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.activities.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activities_list_item, parent, false);

        final Activity activity = this.getItem(position);

        TextView textView = (TextView) rowView.findViewById(R.id.activities_list__item__label);
        textView.setText(activity.getLabel());

        rowView.setTag(R.id.activity_id, activity.getId());
        ActivityList adapter = this;

        Button deleteActivity = (Button) rowView.findViewById(R.id.activities_list__item__delete_button);
        deleteActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ActivityDataSource datasource = new ActivityDataSource(new MySQLite(view.getContext()));
                datasource.delete(activity);
            }
        });

        return rowView;
    }


}
