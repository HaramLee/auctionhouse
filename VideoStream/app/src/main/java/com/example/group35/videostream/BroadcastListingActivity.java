package com.example.group35.videostream;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BroadcastListingActivity extends AppCompatActivity {

    protected ArrayList<String> broadcasts;
   // protected ArrayList<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_listing);

        broadcasts = new ArrayList<String>();
        broadcasts.add("Broadcast 1");
        broadcasts.add("Broadcast 2");
        broadcasts.add("Broadcast 3");
        broadcasts.add("Broadcast 4");
        broadcasts.add("Broadcast 5");
        broadcasts.add("Broadcast 6");
        broadcasts.add("Broadcast 7");
        broadcasts.add("Broadcast 8");
        broadcasts.add("Broadcast 9");
        broadcasts.add("Broadcast 10");
        broadcasts.add("Broadcast 11");

        String[] itemname ={
                "Broadcast 1",
                "Broadcast 2",
                "Broadcast 3",
                "Broadcast 4",
                "Broadcast 5",
                "Broadcast 6",
                "Broadcast 7",
                "Broadcast 8 "
        };

        //images = new ArrayList<Integer>();
        //images.add(R.drawable.ic_launcher);

        Integer[] imgid={
                R.drawable.ic_launcher,

        };

//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, broadcasts);
//        ListView listView = (ListView) findViewById(R.id.broadcastListView);
//        listView.setAdapter(itemsAdapter);

        ListView list;
        CustomListAdapter adapter=new CustomListAdapter(this, itemname,imgid );
        list=(ListView)findViewById(R.id.broadcastListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(BroadcastListingActivity.this, broadcasts.get(position).toString() + " selected.",
//                               Toast.LENGTH_LONG).show();


                startActivity(new Intent(BroadcastListingActivity.this, Layout.class));

            }
        });
    }

    public void onPaymentDetails(final View view) {
        Intent intent = new Intent(this, PaymentDetailsActivity.class);
        startActivity(intent);
    }
}
