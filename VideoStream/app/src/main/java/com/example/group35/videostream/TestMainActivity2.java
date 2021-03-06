package com.example.group35.videostream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class TestMainActivity2 extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private TextView balanceText;
    private TextView nameText;

    private Button payment_button;
    private Button logout_button;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main2);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("First Baptist Church");
        spinnerArray.add("item2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinnerArray);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

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

    }

    @Override
    public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored) {

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

}

