package com.dev.emed;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText fname;
    EditText lname;
    EditText uname;
    EditText pass;
    EditText cpass;
    EditText user_age;
    EditText email;
    EditText phone;
    DatabaseReference reff;
    SignUp_Member signUp_member;

    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        reff = FirebaseDatabase.getInstance().getReference().child("SignUp_Member");

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
                String password = pass.getText().toString(),
                        confirm_password = cpass.getText().toString();

                if(password == confirm_password) {

                    int age = Integer.parseInt(user_age.getText().toString().trim()),
                            phone_no = Integer.parseInt(phone.getText().toString().trim());

                    signUp_member.setFname(fname.getText().toString().trim());
                    signUp_member.setLname(lname.getText().toString().trim());
                    signUp_member.setUname(uname.getText().toString().trim());
                    signUp_member.setPassword(pass.getText().toString().trim());
                    signUp_member.setEmail(email.getText().toString().trim());
                    signUp_member.setAge(age);
                    signUp_member.setPhone(phone_no);

                    reff.push().setValue(signUp_member);
                    Toast.makeText(SignUpActivity.this, "You are all Signed Up", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
