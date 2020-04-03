package com.dev.emed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Switch userTypeSwitch;
    String userType;
    Intent i;

    String userid;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        userTypeSwitch = findViewById(R.id.ptn_doc_switch);

        Button login_redirect = findViewById(R.id.login);
        Button signup_redirect = findViewById(R.id.sign_up);

        login_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = userTypeSwitch.isChecked() ? "Doctor" : "Patient";
                if(userTypeSwitch.isChecked()) {
                    userType = "Doctor";
                    i = new Intent(MainActivity.this, DoctorActivity.class);
                }
                else {
                    userType = "Patient";
                    i = new Intent(MainActivity.this, PatientActivity.class);
                }

                userid = username.getText().toString().trim();
                pass = password.getText().toString().trim();


                if(!userid.isEmpty() && !pass.isEmpty()){
                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference(userType);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(userid).getValue() != null) {
                                Log.d("Pass", dataSnapshot.child(userid).child("password").getValue().toString());

                                String generated_hash = "";
                                try {
                                    MessageDigest md = MessageDigest.getInstance("MD5");
                                    md.update(pass.getBytes());
                                    byte [] bytes = md.digest();
                                    StringBuilder sb = new StringBuilder();
                                    for (byte aByte : bytes)
                                        sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                                    generated_hash = sb.toString();
                                }
                                catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }

                                if(Objects.equals(dataSnapshot.child(userid).child("password").getValue(), generated_hash)) {
                                    i.putExtra("userId", userid);
                                    startActivity(i);
                                }
                                else {
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_login_activity), "Invalid Username/Password", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                }
                            }
                            else {
                                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_login_activity), "Invalid Username/Password", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("Read Failed: ", databaseError.toString());
                        }
                    });
                }
                else {
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
}
