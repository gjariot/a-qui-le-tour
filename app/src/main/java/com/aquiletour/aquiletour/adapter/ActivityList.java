package com.aquiletour.aquiletour.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

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
        textView.setText(
            this.context.getResources().getString(R.string.activities_list_activity_label, activity.getLabel())
        );

        rowView.setTag(R.id.activity_id, activity.getId());
        rowView.setTag(R.id.position, position);
        final ActivityList adapter = this;

        ImageButton deleteActivity = (ImageButton) rowView.findViewById(R.id.activities_list__item__delete_button);
        deleteActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                adapter.buildConfirmationDialog(activity, new Runnable() {
                    public void run() {
                        ActivityDataSource dataSource = new ActivityDataSource(new MySQLite(view.getContext()));
                        dataSource.delete(activity);

                        adapter.activities.remove(activity);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });

        return rowView;
    }

    private AlertDialog buildConfirmationDialog(Activity activity, final Runnable callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        builder.setMessage(this.context.getResources().getString(R.string.activities_list_delete_dialog_message, activity.getLabel()))
                .setTitle(R.string.activities_list_delete_dialog_title);

        builder.setPositiveButton(R.string.activities_list_delete_dialog_confirmation_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                callback.run();
            }
        });
        builder.setNegativeButton(R.string.activities_list_delete_dialog_cancellation_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
