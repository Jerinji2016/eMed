package com.dev.emed;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpPatientActivity extends AppCompatActivity {
    EditText fname;
    EditText lname;
    EditText uname;
    EditText pass;
    EditText cpass;
    EditText user_age;
    EditText email;
    EditText phone;
    DatabaseReference reff;
    SignUp_Patient signUp_patient;

    Button signUpBtn;

    boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);

        signUp_patient = new SignUp_Patient();

        fname = findViewById(R.id.first_name);
        lname = findViewById(R.id.last_name);
        uname = findViewById(R.id.user_name);
        pass = findViewById(R.id.password);
        cpass = findViewById(R.id.confirm_password);
        user_age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        signUpBtn = findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String password = pass.getText().toString();
            String confirm_password = cpass.getText().toString();

            if (checkEmpty()) {
                if(password.equals(confirm_password)) {
                    checkUserAlreadyExist(uname.getText().toString());
                }
                else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.signUp_layout), "Password doesn't Match", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }
        });
    }

    // Function to Check if any field is Empty
    public boolean checkEmpty() {
        if(fname.getText().toString().isEmpty() || lname.getText().toString().isEmpty() ||
                uname.getText().toString().isEmpty() || pass.getText().toString().isEmpty() ||
                cpass.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                user_age.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.signUp_layout), "Please do not leave any field Empty", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            return false;
        }
        return true;
    }

    //  Check if User Name already Exist in User Table
    public void checkUserAlreadyExist(final String user) {
        reff = FirebaseDatabase.getInstance().getReference().child("Patient");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(dataSnapshot.child(user).getValue() != null) {
                   Log.d("Here", "True");
                   setBoolVal(true);
               }
               else {
                   Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.signUp_layout), "Username Available", Snackbar.LENGTH_LONG);
                   snackbar.show();
                   Log.d("Here", "False");
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
        Log.d("Result", ""+ bool);

        if(!bool) {
            reff = FirebaseDatabase.getInstance().getReference().child("Patient");

            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int num = (int) dataSnapshot.getChildrenCount();
                    String pid = "PT00" + num;
                    signUp_patient.setFirst_name(fname.getText().toString().trim());
                    signUp_patient.setLast_name(lname.getText().toString().trim());
                    signUp_patient.setUser_name(uname.getText().toString().trim());
                    signUp_patient.setEmail(email.getText().toString().trim());
                    signUp_patient.setMember_ID(pid);
                    signUp_patient.setAge(Integer.parseInt(user_age.getText().toString().trim()));
                    signUp_patient.setPhone_no(Long.parseLong(phone.getText().toString().trim()));

                    String current_pass = pass.getText().toString().trim(), generatedHash = "";

                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(current_pass.getBytes());
                        byte [] bytes = md.digest();
                        StringBuilder sb = new StringBuilder();
                        for(int i=0; i<bytes.length; i++) {
                            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                        }
                        generatedHash = sb.toString();
                    }
                    catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    signUp_patient.setPassword(generatedHash);

                    reff.child(signUp_patient.getUser_name()).setValue(signUp_patient);

                    Toast.makeText(SignUpPatientActivity.this, "You are all Signed Up", Toast.LENGTH_SHORT).show();

                    //  Should redirect to another activity
                    //  Maintain session
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
                    .make(findViewById(R.id.signUp_layout), "Username Not Available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
