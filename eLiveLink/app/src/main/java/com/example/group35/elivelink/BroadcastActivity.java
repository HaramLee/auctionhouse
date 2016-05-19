package com.example.group35.elivelink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haram on 5/17/2016.
 */
public class BroadcastActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private EditText broadcastEditText;
    private EditText bioEditText;
    private EditText scheduleEditText;
    private EditText youtubeVidIDEditText;
    private EditText subscribeCostEditText;

    private Broadcast currentBroadcast;
    private RequestQueue requestQueue;
    private StringRequest request;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcaster_activity);

        currentBroadcast = new Broadcast();

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        broadcastEditText = (EditText) findViewById(R.id.broadcastEditText);
        bioEditText = (EditText) findViewById(R.id.bioEditText);
        scheduleEditText = (EditText) findViewById(R.id.scheduleEditText);
        youtubeVidIDEditText = (EditText) findViewById(R.id.youtubeEditText);
        subscribeCostEditText = (EditText) findViewById(R.id.priceEditText);

        Intent intent = getIntent();
        currentBroadcast.setYoutubeVidID(intent.getExtras().getString("youtubeVidID"));
        currentBroadcast.setBroadcastName(intent.getExtras().getString("broadcastName"));
        currentBroadcast.setBio(intent.getExtras().getString("bio"));
        currentBroadcast.setSchedule(intent.getExtras().getString("schedule"));
        currentBroadcast.setSubscribeCost(intent.getExtras().getDouble("subscribeCost"));
        currentBroadcast.setBcID(intent.getExtras().getInt("bcID"));

        if(!currentBroadcast.getYoutubeVidID().isEmpty()) {
            youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        }

        populateTextFields();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {

        this.player = player;
        String mVideoId = currentBroadcast.getYoutubeVidID();

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


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), BroadcasterListActivity.class));

    }

    /**
     * Populates the textfields from the currentBroadcast object
     */
    public void populateTextFields() {
        broadcastEditText.setText(currentBroadcast.getBroadcastName());
        bioEditText.setText(currentBroadcast.getBio());
        scheduleEditText.setText(currentBroadcast.getSchedule());
        youtubeVidIDEditText.setText(currentBroadcast.getYoutubeVidID());
        subscribeCostEditText.setText("" + currentBroadcast.getSubscribeCost());
    }

    public void updateCurrentBroadcastWithTextFields() {
        currentBroadcast.setBroadcastName(broadcastEditText.getText().toString());
        currentBroadcast.setBio(bioEditText.getText().toString());
        currentBroadcast.setSchedule(scheduleEditText.getText().toString());
        currentBroadcast.setYoutubeVidID(youtubeVidIDEditText.getText().toString());
        currentBroadcast.setSubscribeCost(Double.parseDouble(subscribeCostEditText.getText().toString()));
    }

    /**
     * Updates the database using the values in currentBroadcast object
     */
    public void updateBroadcast(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("success")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_UPDATE_BROADCAST);
                hashMap.put("bcID", "" + currentBroadcast.getBcID());
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

    /**
     * Toggles if the video is broadcasting or not
     * @param isBroadcasting
     */
    public void toggleBroadcast(final int isBroadcasting){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BROADCAST_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("success")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_TOGGLE_BROADCAST);
                hashMap.put("bcID", "" + currentBroadcast.getBcID());
                hashMap.put("isBroadcasting", "" + isBroadcasting);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Save Button
     * @param view
     */
    public void onSaveButton(final View view) {

        updateCurrentBroadcastWithTextFields();
        updateBroadcast();

        if(!currentBroadcast.getYoutubeVidID().isEmpty()) {

            if(player == null) {
                youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
            }else {
                player.loadVideo(currentBroadcast.getYoutubeVidID());
            }

        }

    }

    /**
     * Start Button
     * @param view
     */
    public void onStartButton(final View view) {

        toggleBroadcast(1);
    }

    /**
     * Stop Button
     * @param view
     */
    public void onStopButton(final View view) {

        toggleBroadcast(0);
    }

    /**
     * Any of the PTZ buttons
     * @param view
     */
    public void onPTZClick(final View view) {
        Toast.makeText(getApplicationContext(), "This feature is currently unavailable.", Toast.LENGTH_SHORT).show();
    }

}