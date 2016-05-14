package com.example.group35.videostream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestMainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;
    private Button payment_button;

    private RequestQueue requestQueue;
    private StringRequest request;
    private List<String> spinnerArray;
    private List<Broadcast> broadcasts;

    private TextView broadcastNameTextView;
    private EditText bioEditText;
    private EditText scheduleEditText;
    private EditText filterEditText;


    private TextView balanceText;
    private TextView nameText;

    private String currentYoutubeID = "6iJu_smJW-o"; //default vid

    private static final String URL = "http://note2myself.cu.ma/broadcast_control.php";
    private static final String DB_QUERY_PASSWORD = "9e3d1f6e3b75eda9922844ca8b0d88b3";
    private static final String GET_BROADCASTS = "getBroadcasts";

    private Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        broadcastNameTextView = (TextView) findViewById(R.id.broadcastNameView);
        bioEditText = (EditText) findViewById(R.id.bioEditText);
        scheduleEditText = (EditText) findViewById(R.id.scheduleEditText);
        filterEditText = (EditText) findViewById(R.id.filterBroadcastEditText);

        broadcasts = new ArrayList<Broadcast>();
        spinnerArray =  new ArrayList<String>();

        getAvailableBroadcasts();

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
    }

    @Override
    public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {

        this.player = player;
        String mVideoId = currentYoutubeID;

        if (mVideoId != null) {
            if (wasRestored) {
                player.play();
            } else {
                player.loadVideo(mVideoId);
            }
        }

    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
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

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAvailableBroadcasts(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
                hashMap.put("db_query_password", DB_QUERY_PASSWORD);
                hashMap.put("db_query_type", GET_BROADCASTS);

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
        }

        initializeSpinner();
    }

    /**
     * Join Broadcast button
     * @param view
     */
    public void onJoinBroadcast(final View view) {
        player.loadVideo(currentYoutubeID);
    }

    /**
     * Filter Button
     * @param view
     */
    public void onFilter(final View view) {

        spinnerArray.clear();

        for(Broadcast a: broadcasts) {
            if(a.getBroadcastName().toLowerCase().contains(filterEditText.getText().toString().toLowerCase())) {
                spinnerArray.add(a.getBroadcastName());
            }
        }

        if(spinnerArray.size() == 0) {
            refreshSpinner();
            Toast.makeText(TestMainActivity.this, "Broadcast name not found.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates the spinner with spinnerArray
     */
    protected void initializeSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(TestMainActivity.this, spinnerArray.get(position).toString(), Toast.LENGTH_LONG).show();

                for(Broadcast a: broadcasts) {
                    if(a.getBroadcastName().equalsIgnoreCase(spinnerArray.get(position).toString())) {
                        broadcastNameTextView.setText(a.getBroadcastName());
                        bioEditText.setText(a.getBio());
                        scheduleEditText.setText(a.getSchedule());
                        currentYoutubeID = a.getYoutubeVidID();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        sItems.setAdapter(adapter);
    }

}

