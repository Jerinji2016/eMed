package com.dev.emed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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
    RadioButton gender;
    Button doc_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctor);

        doc_signup = findViewById(R.id.doc_signup);
        doc_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sign Up Clicked", Toast.LENGTH_SHORT).show();

                pass = findViewById(R.id.doc_pass);
                c_pass = findViewById(R.id.doc_cpass);

                if(pass.getText().toString().trim().equals(c_pass.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password match", Toast.LENGTH_SHORT).show();
                }
                else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.doc_signUp_layout), "Password don't match", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
