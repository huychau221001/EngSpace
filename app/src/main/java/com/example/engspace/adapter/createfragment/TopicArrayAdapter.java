package com.example.engspace.adapter.createfragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.engspace.R;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.TopicResponse;

import java.util.ArrayList;
import java.util.List;

public class TopicArrayAdapter extends ArrayAdapter<TopicResponse> {
    private Activity context;
    private ArrayList<TopicResponse> topicArrayList;

    public TopicArrayAdapter(Activity context, int layoutID, ArrayList<TopicResponse> objects) {
        super(context, layoutID, (List<TopicResponse>) objects);
        this.topicArrayList = objects;
        this.context = context;
    }

    @Override
    public TopicResponse getItem(int position) {
        return topicArrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_list, null,
                    false);
        }

        // Get item
        TopicResponse topic = getItem(position);

        // Get view
        TextView topicName = (TextView) convertView.findViewById(R.id.topic_name);
        topicName.setText(topic.getName());

        return convertView;
    }


}
