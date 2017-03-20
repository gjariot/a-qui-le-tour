package com.aquiletour.aquiletour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aquiletour.aquiletour.R;
import com.aquiletour.aquiletour.entity.Participant;

import java.util.List;

public class ActivityCreationParticipantsList extends BaseAdapter {
    public List<Participant> participants;
    private Context context;

    public ActivityCreationParticipantsList(List<Participant> participants, Context context) {
        this.participants = participants;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.participants.size();
    }

    @Override
    public String getItem(int position) {
        return this.participants.get(position).getName();
    }

    public List<Participant> getParticipants() {
        return this.participants;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.create_activity__participants_list_item, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.create_activity__participants_list__item__label);
        textView.setText(this.getItem(position));

        final ActivityCreationParticipantsList adapter = this;
        final int participantIndex = position;

        ImageButton deleteActivity = (ImageButton) rowView.findViewById(R.id.create_activity__participants_list__item__delete_button);
        deleteActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                adapter.participants.remove(participantIndex);
                adapter.notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
