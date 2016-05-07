package com.example.group35.videostream;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class TestMainActivity3 extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    //    private MyPlayerStateChangeListener playerStateChangeListener;
//    private MyPlaybackEventListener playbackEventListener;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main3);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("First Baptist Church");
        spinnerArray.add("item2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

//        playerStateChangeListener = new MyPlayerStateChangeListener();
//        playbackEventListener = new MyPlaybackEventListener();
//          youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
//          youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

//        final EditText seekToText = (EditText) findViewById(R.id.seek_to_text);
//        Button seekToButton = (Button) findViewById(R.id.seek_to_button);
//        seekToButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int skipToSecs = Integer.valueOf(seekToText.getText().toString());
//                player.seekToMillis(skipToSecs * 1000);
//            }
//        });

    }

    @Override
    public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {
//        player.setPlayerStateChangeListener(playerStateChangeListener);
//        player.setPlaybackEventListener(playbackEventListener);
        this.player = player;
        String mVideoId = "r_KlltnQJbQ";

        if (mVideoId != null) {
            if (wasRestored) {
                player.play();
            } else {
                player.loadVideo(mVideoId);
            }
        }


//        if (!wasRestored) {
//            player.cueVideo("vtg4o__aRMg"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
//        } else if (wasRestored) {
//            player.play();
//        } else {
//            player.loadVideo("vtg4o__aRMg");
//        }
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
//
//    private void showMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//    }
//
//    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
//
//        @Override
//        public void onPlaying() {
//            // Called when playback starts, either due to user action or call to play().
//            showMessage("Playing");
//        }
//
//        @Override
//        public void onPaused() {
//            // Called when playback is paused, either due to user action or call to pause().
//            showMessage("Paused");
//        }
//
//        @Override
//        public void onStopped() {
//            // Called when playback stops for a reason other than being paused.
//            showMessage("Stopped");
//        }
//
//        @Override
//        public void onBuffering(boolean b) {
//            // Called when buffering starts or ends.
//        }
//
//        @Override
//        public void onSeekTo(int i) {
//            // Called when a jump in playback position occurs, either
//            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
//        }
//    }
//
//    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
//
//        @Override
//        public void onLoading() {
//            // Called when the player is loading a video
//            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
//        }
//
//        @Override
//        public void onLoaded(String s) {
//            // Called when a video is done loading.
//            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
//        }
//
//        @Override
//        public void onAdStarted() {
//            // Called when playback of an advertisement starts.
//        }
//
//        @Override
//        public void onVideoStarted() {
//            // Called when playback of the video starts.
//        }
//
//        @Override
//        public void onVideoEnded() {
//            // Called when the video reaches its end.
//        }
//
//        @Override
//        public void onError(YouTubePlayer.ErrorReason errorReason) {
//            // Called when an error occurs.
//        }
//    }
}
