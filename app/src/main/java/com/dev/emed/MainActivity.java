package com.dev.emed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.emed.doctor.DoctorActivity;
import com.dev.emed.patient.PatientActivity;
import com.dev.emed.signup.SelectUserTypeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText username;
    EditText password;
    Switch userTypeSwitch;
    String userType;
    Intent i;

    String userid;
    String pass;
    boolean rememberMe = false;

    DatabaseReference reff;
    ValueEventListener valListenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        userTypeSwitch = findViewById(R.id.ptn_doc_switch);

        Button login_redirect = findViewById(R.id.login);
        Button signup_redirect = findViewById(R.id.sign_up);


        ArrayList<String> arr = new ArrayList<>();
        arr.add("apple");
        arr.add("banana");
        arr.add("orange");

        final CheckBox rememberCheck = findViewById(R.id.remember_login_chip);

        rememberCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberMe = isChecked;
            }
        });

        login_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
                if (userTypeSwitch.isChecked()) {
                    userType = "Doctor";
                    i = new Intent(MainActivity.this, DoctorActivity.class);
                } else {
                    userType = "Patient";
                    i = new Intent(MainActivity.this, PatientActivity.class);
                }

                userid = username.getText().toString().trim();
                pass = password.getText().toString().trim();


                if (!userid.isEmpty() && !pass.isEmpty()) {
                    reff = FirebaseDatabase.getInstance().getReference(userType);
                    valListenter = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(userid).getValue() != null) {
                                Log.d("Pass", dataSnapshot.child(userid).child("password").getValue().toString());

                                String generated_hash = "";
                                try {
                                    MessageDigest md = MessageDigest.getInstance("MD5");
                                    md.update(pass.getBytes());
                                    byte[] bytes = md.digest();
                                    StringBuilder sb = new StringBuilder();
                                    for (byte aByte : bytes)
                                        sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                                    generated_hash = sb.toString();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }

                                if (Objects.equals(dataSnapshot.child(userid).child("password").getValue(), generated_hash)) {
                                    //  Logging In

                                    if (rememberMe) {
                                        SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("userId", userid);
                                        editor.putString("password", generated_hash);
                                        editor.putString("userType", userType);
                                        editor.putBoolean("rememberMe", true);
                                        editor.apply();

                                        Log.d(TAG, "onDataChange: Remembered Login");
                                    } else {
                                        SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("userId", userid);
                                        editor.putBoolean("rememberMe", false);
                                        editor.apply();
                                    }

                                    i.putExtra("userId", userid);
                                    startActivity(i);
                                } else {
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_login_activity), "Invalid Username/Password", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_login_activity), "Invalid Username/Password", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("Read Failed: ", databaseError.toString());
                        }
                    };

                    reff.addListenerForSingleValueEvent(valListenter);
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_login_activity), "Please dont leave the fields Empty", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        signup_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity.this, SelectUserTypeActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        Log.d(TAG, "onPause: Activity Paused");
    }
}