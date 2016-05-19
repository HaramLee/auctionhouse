package com.example.group35.elivelink;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Haram on 2016-05-19.
 */
public class Broadcaster_CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<String> broadcastNames;
    private List<String> scheduleArray;
    private final Integer[] imgid;

    public Broadcaster_CustomListAdapter(Activity context,List<String> broadcastNames, List<String> scheduleArray, Integer[] imgid) {
        super(context, R.layout.broadcaster_custom_list, broadcastNames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.broadcastNames=broadcastNames;
        this.scheduleArray=scheduleArray;
        this.imgid=imgid;
    }


    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.broadcaster_custom_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        imageView.setImageResource(imgid[0]);
        txtTitle.setText(broadcastNames.get(position));
        extratxt.setText(scheduleArray.get(position));
        return rowView;

    };
}