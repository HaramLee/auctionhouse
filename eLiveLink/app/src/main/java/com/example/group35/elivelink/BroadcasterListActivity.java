package com.example.group35.elivelink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

public class BroadcasterListActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private StringRequest request;

    ListView broadcastListView;

    private List<Broadcast> broadcasts;
    private List<String> broadcastNames;

    private int userID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcaster_list);

        getSupportActionBar().setTitle("Balance: $" + MainActivity.balance);

        broadcasts = new ArrayList<>();
        broadcastNames = new ArrayList<>();
        broadcastListView = (ListView) findViewById(R.id.broadcastListView);

        getUserBroadcasts();

    }


    /**
     * Get the broadcasts of the specific user
     */
    public void getUserBroadcasts(){

        broadcasts.clear();

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        //Toast.makeText(getApplicationContext(), "Success " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                                tempBroadcast.setIsBroadcasting(((JSONObject) jsonBroadcasts.get(key)).getInt("isBroadcasting"));

                                broadcasts.add(tempBroadcast);
                            }
                        }

                        initializeListView();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_GET_BROADCASTS_WITH_USERID);
                hashMap.put("userID", "" + userID);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Create new untitled broadcast for the user
     */
    public void createBroadcast(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getUserBroadcasts();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_CREATE_BROADCAST);
                hashMap.put("userID", "" + userID);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Delete broadcast with bcID. Then refreshes the ListView
     * @param bcID
     */
    public void deleteBroadcast(final int bcID){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getUserBroadcasts();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_DELETE_BROADCAST);
                hashMap.put("bcID", "" + bcID);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }


    /**
     * Creates the listview with broadcast array
     * When you click an item, you get prompted for Edit or Delete
     */
    protected void initializeListView() {

        broadcastNames.clear();

        for(Broadcast a: broadcasts) {
            broadcastNames.add(a.getBroadcastName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BroadcasterListActivity.this, android.R.layout.simple_list_item_1, broadcastNames);
        broadcastListView.setAdapter(adapter);
        broadcastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, final int pos, long arg3) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BroadcasterListActivity.this);
                builder.setMessage(broadcasts.get(pos).getBroadcastName())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), "Delete " + broadcasts.get(pos).getBroadcastName(), Toast.LENGTH_SHORT).show();
                                deleteBroadcast(broadcasts.get(pos).getBcID());
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getApplicationContext(), "Edit " + broadcasts.get(pos).getBroadcastName(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), BroadcastActivity.class);
                                intent.putExtra("bcID",broadcasts.get(pos).getBcID());
                                intent.putExtra("broadcastName",broadcasts.get(pos).getBroadcastName());
                                intent.putExtra("bio",broadcasts.get(pos).getBio());
                                intent.putExtra("schedule",broadcasts.get(pos).getSchedule());
                                intent.putExtra("youtubeVidID",broadcasts.get(pos).getYoutubeVidID());

                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    /**
     * Create Broadcast Button
     * @param view
     */
    public void onCreateBroadcast(final View view) {

        createBroadcast();

    }
}
