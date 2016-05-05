package com.example.group35.videostream;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PaymentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
    }

    public void onSave(final View view) {
        Toast.makeText(this, "Data saved!", Toast.LENGTH_LONG).show();
    }
}
