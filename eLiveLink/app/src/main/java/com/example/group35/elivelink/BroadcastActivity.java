package com.example.group35.elivelink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

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
public class BroadcastActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private TextView balanceText;
    private TextView nameText;

    private Button payment_button;
    private Button logout_button;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;
    private Broadcast currentBroadcast;
    private int userID;

    private RequestQueue requestQueue;
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcaster_activity);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        payment_button = (Button) findViewById(R.id.button_payment);
        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentDetailsActivity.class));
            }

        });

        logout_button = (Button) findViewById(R.id.logout_but);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = MainActivity.mPreferences.edit();
                editor.clear();   // This will delete all your preferences, check how to delete just one
                editor.commit();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

        });

        balanceText = (TextView) findViewById(R.id.textView2);
        balanceText.setText(MainActivity.balance);

        nameText = (TextView) findViewById(R.id.textView);
        nameText.setText(MainActivity.name);

        userID = 1;
        getUserBroadcast();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {

        this.player = player;
        String mVideoId = "r_KlltnQJbQ";

        if (mVideoId != null) {
            if (wasRestored) {
                player.play();
            } else {
                player.loadVideo(mVideoId);
            }
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }


    public void getUserBroadcast(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "Success " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                        currentBroadcast = new Broadcast();
                        currentBroadcast.setBcID(jsonObject.getInt("bcID"));
                        currentBroadcast.setBio(jsonObject.getString("bio"));
                        currentBroadcast.setBroadcastName(jsonObject.getString("broadcastName"));
                        currentBroadcast.setSchedule(jsonObject.getString("schedule"));
                        currentBroadcast.setYoutubeVidID(jsonObject.getString("youtubeVidID"));
                        currentBroadcast.setSubscribeCost(jsonObject.getDouble("subscribeCost"));
                        currentBroadcast.setIsBroadcasting(jsonObject.getInt("isBroadcasting"));

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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_GET_BROADCASTS_WITH_USERID);
                hashMap.put("userID", "" + userID);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    public void updateBroadcast(final View view){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "Success " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_UPDATE_BROADCAST);
                hashMap.put("userID", "" + userID);
                hashMap.put("broadcastName", currentBroadcast.getBroadcastName());
                hashMap.put("youtubeVidID", currentBroadcast.getYoutubeVidID());
                hashMap.put("subscribeCost", "" + currentBroadcast.getSubscribeCost());
                hashMap.put("bio", currentBroadcast.getBio());
                hashMap.put("schedule", currentBroadcast.getSchedule());

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    public void startBroadcast(final View view){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "Success " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_TOGGLE_BROADCAST);
                hashMap.put("userID", "" + userID);
                hashMap.put("isBroadcasting", "" + 1);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    public void stopBroadcast(final View view){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "Success " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_TOGGLE_BROADCAST);
                hashMap.put("userID", "" + userID);
                hashMap.put("isBroadcasting", "" + 0);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }


}
