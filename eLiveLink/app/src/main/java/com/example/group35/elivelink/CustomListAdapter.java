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
 * Created by Haram on 5/17/2016.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<String> spinnerArray;
    private List<String> scheduleArray;
    private List<Double> streamPriceArray;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context,List<String> spinnerArray,
                             List<String> scheduleArray, List<Double> streamPriceArray , Integer[] imgid) {
        super(context, R.layout.custom_list, spinnerArray);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.spinnerArray=spinnerArray;
        this.scheduleArray=scheduleArray;
        this.streamPriceArray = streamPriceArray;
        this.imgid=imgid;
    }


    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        TextView priceText = (TextView) rowView.findViewById(R.id.textView);

        txtTitle.setText(spinnerArray.get(position));
        extratxt.setText(scheduleArray.get(position));
        priceText.setText("$" + (streamPriceArray.get(position)));
        return rowView;

    }

}
