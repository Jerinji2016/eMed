package com.dev.emed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectUserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);

        Button docBtn = findViewById(R.id.doctor_btn);
        Button patientBtn = findViewById(R.id.patient_btn);

        docBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectUserTypeActivity.this, SignUp_Doctor.class);
                startActivity(i);
            }
        });

        patientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectUserTypeActivity.this, SignUpPatientActivity.class);
                startActivity(i);
            }
        });
    }
}
