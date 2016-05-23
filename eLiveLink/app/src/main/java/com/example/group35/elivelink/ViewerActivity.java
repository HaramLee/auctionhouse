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

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Haram on 5/17/2016.
 */
public class ViewerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private TextView broadcastNameTextView;
    private EditText bioEditText;
    private EditText scheduleEditText;

    private String current_vid;
    private String broad_name;
    private String broad_bio;
    private String broad_schedule;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer_activity);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        Intent intent = getIntent();
        current_vid = intent.getExtras().getString("broadcaster_youtube");
        broad_name = intent.getExtras().getString("broadcaster_name");
        broad_bio = intent.getExtras().getString("broadcaster_bio");
        broad_schedule = intent.getExtras().getString("broadcaster_schedule");

        broadcastNameTextView = (TextView) findViewById(R.id.broadcastNameView);
        bioEditText = (EditText) findViewById(R.id.bioEditText);
        scheduleEditText = (EditText) findViewById(R.id.scheduleEditText);

        broadcastNameTextView.setText(broad_name);
        bioEditText.setText(broad_bio);
        scheduleEditText.setText(broad_schedule);

        if((!current_vid.isEmpty()) || current_vid != null) {
            youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {

        this.player = player;
        String mVideoId = current_vid;

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ListActivity.class));
    }

}

