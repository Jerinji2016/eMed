package com.dev.emed.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.emed.R;
import com.dev.emed.qrCode.models.PatientDetailObject;
import com.google.gson.Gson;

public class PatientDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PatientDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        Log.d(TAG, "onCreate: PatientDetails");

        Intent i = getIntent();
        String decryptedString = i.getStringExtra("dcy_text");

        PatientDetailObject patient = new Gson()
                .fromJson(decryptedString, PatientDetailObject.class);

        TextView nameText = findViewById(R.id.ptn_detail_name);
        TextView userIdText = findViewById(R.id.ptn_detail_userid);
        TextView reffIdText = findViewById(R.id.ptn_detail_reffid);
        TextView ageText = findViewById(R.id.ptn_detail_age);
        TextView genderText = findViewById(R.id.ptn_detail_gender);
        TextView weightText = findViewById(R.id.ptn_detail_weight);
        TextView heightText = findViewById(R.id.ptn_detial_height);
        TextView medConditionText = findViewById(R.id.ptn_detail_medCondition);

        nameText.setText(patient.getName());
        userIdText.setText(patient.getUserId());
        reffIdText.setText(patient.getReffId());
        ageText.setText(patient.getAge());
        genderText.setText(patient.getGender());
        weightText.setText(patient.getWeight());
        heightText.setText(patient.getHeight());
        medConditionText.setText(patient.getMedConditions());

        Button cancelPres = findViewById(R.id.cancel_prescription);
        cancelPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        Button showHistory = findViewById(R.id.show_ptn_med_history_btn);
        final LinearLayout medHistoryLayout = findViewById(R.id.ptn_detail_med_history_layout);
        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medHistoryLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
