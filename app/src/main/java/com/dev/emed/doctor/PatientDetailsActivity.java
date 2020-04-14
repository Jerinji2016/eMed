package com.dev.emed.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.emed.R;
import com.dev.emed.qrCode.models.PatientDetailObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class PatientDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PatientDetailsActivity";
    String myPtn;

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
        final TextView reffIdText = findViewById(R.id.ptn_detail_reffid);
        final TextView ageText = findViewById(R.id.ptn_detail_age);
        final TextView genderText = findViewById(R.id.ptn_detail_gender);
        final TextView weightText = findViewById(R.id.ptn_detail_weight);
        final TextView heightText = findViewById(R.id.ptn_detial_height);
        final TextView medConditionText = findViewById(R.id.ptn_detail_medCondition);

        nameText.setText(patient.getName());
        userIdText.setText(patient.getUserId());

        Button cancelPres = findViewById(R.id.cancel_prescription);
        cancelPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        myPtn = patient.getUserId();
        DatabaseReference mReff = FirebaseDatabase.getInstance().getReference("Patient");
        mReff.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ptnDetails = dataSnapshot.child(myPtn);

                reffIdText.setText((String) ptnDetails.child("member_ID").getValue());

                long age = (long) ptnDetails.child("age").getValue();
                ageText.setText(Long.toString(age));
                genderText.setText((String) ptnDetails.child("gender").getValue());
                weightText.setText((String) ptnDetails.child("physic").child("weight").getValue());;
                heightText.setText((String) ptnDetails.child("physic").child("height").getValue());;
                medConditionText.setText((String) ptnDetails.child("medicalConditions").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
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
