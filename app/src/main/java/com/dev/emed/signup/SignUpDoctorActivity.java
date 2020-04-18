package com.dev.emed.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dev.emed.R;
import com.dev.emed.doctor.DoctorActivity;
import com.dev.emed.models.SignUp_Doctor;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpDoctorActivity extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText specialisation;
    EditText hosp_name;
    EditText hosp_addr;
    EditText uname;
    EditText pass;
    EditText c_pass;
    EditText phone;
    EditText email;
    RadioGroup gender;
    RadioButton selected_gender;

    SignUp_Doctor signUp_doctor;

    Button signUpBtn;
    boolean bool = false;

    DatabaseReference reff = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctor);

        signUp_doctor = new SignUp_Doctor();

        fname = findViewById(R.id.doc_fname);
        lname = findViewById(R.id.doc_lname);
        specialisation = findViewById(R.id.doc_specialisation);
        hosp_name = findViewById(R.id.doc_hosp_name);
        hosp_addr = findViewById(R.id.doc_hosp_address);
        uname = findViewById(R.id.doc_uname);
        pass = findViewById(R.id.doc_pass);
        c_pass = findViewById(R.id.doc_cpass);
        phone = findViewById(R.id.doc_phone);
        email = findViewById(R.id.doc_email);
        gender = findViewById(R.id.doc_gender);

        signUpBtn = findViewById(R.id.doc_signup);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()) {
                    if(pass.getText().toString().trim().equals(c_pass.getText().toString().trim())) {
                        checkUserAlreadyExist(uname.getText().toString().trim());
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.doc_signUp_layout), "Password don't match", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    public boolean checkEmpty() {
        if (fname.getText().toString().isEmpty() || lname.getText().toString().isEmpty() ||
                specialisation.getText().toString().isEmpty() || hosp_name.getText().toString().isEmpty() ||
                hosp_addr.getText().toString().isEmpty() || uname.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
            return false;
        }
        RadioButton m = findViewById(R.id.doc_radio_male),
                    f = findViewById(R.id.doc_radio_female),
                    o = findViewById(R.id.doc_radio_other);
        return !(!m.isChecked() && !f.isChecked() && !o.isChecked());
    }

    public void checkUserAlreadyExist(final String user) {
        reff = FirebaseDatabase.getInstance().getReference().child("Doctor");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(user).getValue() != null) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.doc_signUp_layout), "Username Unavailable", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    setBoolVal(true);
                }
                else {
                    setBoolVal(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Executive Order", "The read failed: " + databaseError.getDetails());
            }
        });
    }

    public void setBoolVal(boolean b) {
        bool = b;
        signUpUser();
    }

    public void signUpUser() {
        if(!bool) {
            reff = FirebaseDatabase.getInstance().getReference().child("Doctor");

            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    signUpBtn.setEnabled(false);

                    Log.d("d-","Signing Up");
                    int num = (int) dataSnapshot.getChildrenCount();
                    String pid = "DC00" + num;
                    Log.d("name- ", fname.getText().toString().trim() + " " + lname.getText().toString().trim());
                    signUp_doctor.setName(fname.getText().toString().trim() + " " + lname.getText().toString().trim());
                    signUp_doctor.setUser_name(uname.getText().toString().trim());
                    signUp_doctor.setEmail(email.getText().toString().trim());
                    signUp_doctor.setMember_ID(pid);
                    signUp_doctor.setPhone_no(Long.parseLong(phone.getText().toString().trim()));
                    signUp_doctor.setSpecialisation(specialisation.getText().toString().trim());
                    signUp_doctor.setHospital_address(hosp_addr.getText().toString().trim());
                    signUp_doctor.setHospital_name(hosp_name.getText().toString().trim());

                    selected_gender = findViewById(gender.getCheckedRadioButtonId());
                    signUp_doctor.setGender((String) selected_gender.getText());

                    String current_pass = pass.getText().toString().trim(), generatedHash = "";

                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(current_pass.getBytes());
                        byte [] bytes = md.digest();
                        StringBuilder sb = new StringBuilder();
                        for (byte aByte : bytes)
                            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                        generatedHash = sb.toString();
                    }
                    catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    signUp_doctor.setPassword(generatedHash);
                    reff.child(signUp_doctor.getUser_name()).setValue(signUp_doctor);

                    Toast.makeText(SignUpDoctorActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpDoctorActivity.this, DoctorActivity.class);
                    i.putExtra("userId", signUp_doctor.getUser_name());
                    startActivity(i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Error", "DB-Error"+databaseError.getDetails());
                }
            });
        }
        else {
            uname.setText("");
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.doc_signUp_layout), "Username Not Available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
