package com.example.group35.elivelink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Haram on 5/17/2016.
 */
public class ListActivity extends AppCompatActivity {

    private List<Broadcast> broadcasts;
    private List<String> spinnerArray;
    private List<String> scheduleArray;

    private RequestQueue requestQueue;
    private StringRequest request;

    private EditText filterEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_list);


        getSupportActionBar().setTitle("Balance: $" + MainActivity.balance);

        filterEditText = (EditText) findViewById(R.id.filterBroadcastEditText);

        broadcasts = new ArrayList<Broadcast>();
        spinnerArray =  new ArrayList<String>();
        scheduleArray =  new ArrayList<String>();

        getAvailableBroadcasts();

    }

    public void onPaymentDetails(final View view) {
        Intent intent = new Intent(this, PaymentDetailsActivity.class);
        startActivity(intent);
    }

    public void getAvailableBroadcasts(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "Success " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonBroadcasts = jsonObject.getJSONObject("broadcastList");

                        Iterator<?> keys = jsonBroadcasts.keys();

                        while( keys.hasNext() ) {
                            String key = (String) keys.next();

                            if ( jsonBroadcasts.get(key) instanceof JSONObject ) {
                                System.out.println(jsonBroadcasts.get(key).toString());

                                Broadcast tempBroadcast = new Broadcast();
                                tempBroadcast.setBcID(((JSONObject) jsonBroadcasts.get(key)).getInt("bcID"));
                                tempBroadcast.setBio(((JSONObject) jsonBroadcasts.get(key)).getString("bio"));
                                tempBroadcast.setBroadcastName(((JSONObject) jsonBroadcasts.get(key)).getString("broadcastName"));
                                tempBroadcast.setSchedule(((JSONObject) jsonBroadcasts.get(key)).getString("schedule"));
                                tempBroadcast.setYoutubeVidID(((JSONObject) jsonBroadcasts.get(key)).getString("youtubeVidID"));

                                broadcasts.add(tempBroadcast);
                            }
                        }

                        refreshSpinner();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("db_query_password", Config.DB_QUERY_PASSWORD);
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_GET_BROADCASTS);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Refreshes the spinner list using broadcast list
     */
    public void refreshSpinner() {

        spinnerArray.clear();

        for(Broadcast a: broadcasts) {
            spinnerArray.add(a.getBroadcastName());
            scheduleArray.add(a.getSchedule());
        }

        initializeSpinner();
    }

    /**
     * Creates the spinner with spinnerArray
     */
    protected void initializeSpinner(){

        Integer[] imgid={
                R.drawable.ic_launcher,

        };

        ListView list;
        CustomListAdapter adapter=new CustomListAdapter(this, spinnerArray, scheduleArray ,imgid );
        list=(ListView)findViewById(R.id.broadcastListView);
        list.setAdapter(adapter);

        final Intent to_viewer = new Intent(this, ViewerActivity.class);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Broadcast a : broadcasts) {
                    if (a.getBroadcastName().equalsIgnoreCase(spinnerArray.get(position).toString())) {
                        to_viewer.putExtra("broadcaster_name", a.getBroadcastName());
                        to_viewer.putExtra("broadcaster_bio", a.getBio());
                        to_viewer.putExtra("broadcaster_schedule", a.getSchedule());
                        to_viewer.putExtra("broadcaster_youtube", a.getYoutubeVidID());
                        break;
                    }
                }

                startActivity(to_viewer);


            }
        });
    }

    public void onFilter(final View view) {

        spinnerArray.clear();
        scheduleArray.clear();

        for(Broadcast a: broadcasts) {
            if(a.getBroadcastName().toLowerCase().contains(filterEditText.getText().toString().toLowerCase())) {
                spinnerArray.add(a.getBroadcastName());
                scheduleArray.add(a.getSchedule());
            }
        }

        if(spinnerArray.size() == 0) {
            refreshSpinner();
            Toast.makeText(ListActivity.this, "Broadcast name not found.", Toast.LENGTH_SHORT).show();
        }

        initializeSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_logout, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_settings:
                Intent payment_activity = new Intent(ListActivity.this, PaymentDetailsActivity.class);
                startActivity(payment_activity);
                break;
            case R.id.logout_settings:
                SharedPreferences.Editor editor = MainActivity.mPreferences.edit();
                editor.clear();   // This will delete all your preferences, check how to delete just one
                editor.commit();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
