package com.example.group35.elivelink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener  {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    private RequestQueue requestQueue;
    private StringRequest request;

    private EditText password_text;
    private EditText email_text;
    private Button sign_in_register;

    public static SharedPreferences mPreferences;
    public static String name;
    public static String isViewer;
    public static String balance;

    static final String KEY_USERNAME = "login_name";
    static final String KEY_PASSWORD = "login_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        email_text = (EditText) findViewById(R.id.email);
        password_text = (EditText) findViewById(R.id.password);
        sign_in_register = (Button) findViewById(R.id.sign_in);

        requestQueue = Volley.newRequestQueue(this);

        mPreferences = getSharedPreferences("User", MODE_PRIVATE);
        final SharedPreferences.Editor editor = mPreferences.edit();


        sign_in_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                request = new StringRequest(Request.Method.POST, Config.DB_USER_CONTROL_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {

                                editor.putString("username", "key");

                                isViewer = jsonObject.getString("isViewer");
                                balance = jsonObject.getString("accountBalance");

                                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                                        R.style.AppTheme_Dark_Dialog);

                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Authenticating...");
                                progressDialog.show();

                                String emailSave = email_text.getText().toString();
                                String passwordSave = password_text.getText().toString();

                                editor.putString(KEY_USERNAME, emailSave);
                                editor.putString(KEY_PASSWORD, passwordSave);
                                editor.commit();

                                // TODO: Implement your own authentication logic here.

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                // On complete call either onLoginSuccess or onLoginFailed
                                                // onLoginFailed();
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);

                                if(jsonObject.getInt("isViewer")== 0){
                                    Toast.makeText(getApplicationContext(), "Success: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), BroadcastActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Success: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Error: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("db_query_password", Config.DB_QUERY_PASSWORD);
                        hashMap.put("db_query_type", Config.DB_QUERY_TYPE_USER_LOGIN);
                        hashMap.put("email", email_text.getText().toString());
                        hashMap.put("password", password_text.getText().toString());

                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });

        if (mPreferences.contains("username")) {
            // start Main activity

            String _username = mPreferences.getString(KEY_USERNAME, "");
            String _password = mPreferences.getString(KEY_PASSWORD, ""); //return nothing if no pass saved
            email_text.setText(_username);
            password_text.setText(_password);
            auto_login_user();

        }
    }


    public void auto_login_user(){

        requestQueue = Volley.newRequestQueue(this);
        request = new StringRequest(Request.Method.POST, Config.DB_USER_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {

                        name = jsonObject.getString("username");
                        isViewer = jsonObject.getString("isViewer");
                        balance = jsonObject.getString("accountBalance");

                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                                R.style.AppTheme_Dark_Dialog);

                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Authenticating...");
                        progressDialog.show();

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onLoginSuccess or onLoginFailed
                                        // onLoginFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);

                        if(jsonObject.getInt("isViewer")== 0){
                            Toast.makeText(getApplicationContext(), "Success: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), BroadcastActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Success: " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ListActivity.class));
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("db_query_password", Config.DB_QUERY_PASSWORD);
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_USER_LOGIN);
                hashMap.put("email", email_text.getText().toString());
                hashMap.put("password", password_text.getText().toString());

                return hashMap;
            }
        };
        requestQueue.add(request);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {

        this.player = player;
        String mVideoId = Config.DEFAULT_VIDEO;

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
        moveTaskToBack(true);
    }
}
