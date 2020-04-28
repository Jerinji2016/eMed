package com.dev.emed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dev.emed.doctor.DoctorActivity;
import com.dev.emed.patient.PatientActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences checkPref = getSharedPreferences("rememberLogin", MODE_PRIVATE);
                if (checkPref.getBoolean("rememberMe", false)) {

                    final String id = checkPref.getString("userId", ""),
                            pass = checkPref.getString("password", ""),
                            uType = checkPref.getString("userType", "");

                    DatabaseReference pReff = FirebaseDatabase.getInstance().getReference(uType);
                    pReff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(id).getValue() != null && pass.equals(dataSnapshot.child(id).child("password").getValue())) {
                                Intent intent;
                                intent = new Intent(HomeActivity.this, (uType.equals("Doctor")) ? DoctorActivity.class : PatientActivity.class);
                                intent.putExtra("userId", id);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
