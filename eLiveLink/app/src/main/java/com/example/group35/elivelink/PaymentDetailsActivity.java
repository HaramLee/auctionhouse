package com.example.group35.elivelink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class PaymentDetailsActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private StringRequest request;

    private EditText billNameEditText;
    private EditText billAddressEditText;
    private EditText billPCodeEditText;
    private EditText ccNumEditText;
    private EditText ccExpiryEditText;
    private EditText ccCVVEditText;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        billNameEditText = (EditText) findViewById(R.id.nameOnCardEditText);
        billAddressEditText = (EditText) findViewById(R.id.billingAddressEditText);
        billPCodeEditText = (EditText) findViewById(R.id.postalCodeEditText);
        ccNumEditText = (EditText) findViewById(R.id.cardNumberEditText);
        ccExpiryEditText = (EditText) findViewById(R.id.expiryDateEditText);
        ccCVVEditText = (EditText) findViewById(R.id.cvvEditText);

        Intent intent = getIntent();
        userID = intent.getExtras().getInt("userID");

        getBillingInformation();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onSave(final View view) {
        //Toast.makeText(this, "Data saved!", Toast.LENGTH_LONG).show();
        updateBillingInformation();
        finish();
    }

    /**
     * Updates the database using the values in payment details fields
     */
    public void getBillingInformation(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BILLING_CONTROL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("success")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                        billNameEditText.setText(jsonObject.getString("billName"));
                        billAddressEditText.setText(jsonObject.getString("billAddress"));
                        billPCodeEditText.setText(jsonObject.getString("billPCode"));
                        ccNumEditText.setText(jsonObject.getString("ccNum"));
                        ccExpiryEditText.setText(jsonObject.getString("ccExpiry"));
                        ccCVVEditText.setText(jsonObject.getString("ccCVV"));

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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_GET_BILLING);
                hashMap.put("userID", "" + userID);

                return hashMap;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Updates the database using the values in payment details fields
     */
    public void updateBillingInformation(){

        requestQueue = Volley.newRequestQueue(this);

        request = new StringRequest(Request.Method.POST, Config.DB_BILLING_CONTROL_URL, new Response.Listener<String>() {
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
                hashMap.put("db_query_type", Config.DB_QUERY_TYPE_SET_BILLING);
                hashMap.put("userID", "" + userID);
                hashMap.put("billName", billNameEditText.getText().toString());
                hashMap.put("billAddress", billAddressEditText.getText().toString());
                hashMap.put("billPCode", billPCodeEditText.getText().toString() );
                hashMap.put("ccNum", ccNumEditText.getText().toString() );
                hashMap.put("ccExpiry", ccExpiryEditText.getText().toString());
                hashMap.put("ccCVV", ccCVVEditText.getText().toString());

                return hashMap;
            }
        };

        requestQueue.add(request);
    }
}
