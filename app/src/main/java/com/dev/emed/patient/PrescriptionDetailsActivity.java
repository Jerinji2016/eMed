package com.dev.emed.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dev.emed.R;
import com.dev.emed.qrCode.models.PatientDetailObject;
import com.dev.emed.qrCode.models.PrescriptionObject;
import com.google.gson.Gson;

public class PrescriptionDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PrescriptionDetailsActi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        Log.d(TAG, "onCreate: Prescription Page");

        Intent i = getIntent();
        String decryptedString = i.getStringExtra("dcy_text");

        String userObj = new Gson()
                .fromJson(decryptedString, String.class);

        Toast.makeText(this, userObj, Toast.LENGTH_SHORT).show();
    }
}
