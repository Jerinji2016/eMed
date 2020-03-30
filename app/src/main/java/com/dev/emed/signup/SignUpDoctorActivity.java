package com.dev.emed.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dev.emed.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    RadioGroup gender;
    RadioButton selected_gender;
    Button doc_signup;

    DatabaseReference reff = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctor);

        fname = findViewById(R.id.doc_fname);
        lname = findViewById(R.id.doc_lname);
        specialisation = findViewById(R.id.doc_specialisation);
        hosp_name = findViewById(R.id.doc_hosp_name);
        hosp_addr = findViewById(R.id.doc_hosp_address);
        uname = findViewById(R.id.doc_uname);
        pass = findViewById(R.id.doc_pass);
        c_pass = findViewById(R.id.doc_cpass);
        phone = findViewById(R.id.doc_phone);
        gender = findViewById(R.id.doc_gender);

        doc_signup = findViewById(R.id.doc_signup);
        doc_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sign Up Clicked", Toast.LENGTH_SHORT).show();

                if(pass.getText().toString().trim().equals(c_pass.getText().toString().trim())) {
                    if(checkEmpty()) {
                        Toast.makeText(getApplicationContext(), "Gender Probs", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        selected_gender = findViewById(gender.getCheckedRadioButtonId());
                        Toast.makeText(getApplicationContext(), selected_gender.getText().toString(), Toast.LENGTH_SHORT).show();
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
//        if (fname.getText().toString().isEmpty() || lname.getText().toString().isEmpty() ||
//                specialisation.getText().toString().isEmpty() || hosp_name.getText().toString().isEmpty() ||
//                hosp_addr.getText().toString().isEmpty() || uname.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
//            Log.d("hjbd", "ksdh");
//            return true;
//        }
        RadioButton m = findViewById(R.id.doc_radio_male), f = findViewById(R.id.doc_radio_female), o = findViewById(R.id.doc_radio_other);
    if(!m.isChecked() && !f.isChecked() && !o.isChecked()) {
        return true;
    }
        return false;
    }
}
