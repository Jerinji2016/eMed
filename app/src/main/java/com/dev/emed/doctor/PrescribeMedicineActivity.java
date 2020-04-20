package com.dev.emed.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dev.emed.R;

public class PrescribeMedicineActivity extends AppCompatActivity {

    private static final String TAG = "PrescribeMedActivity";
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribe_medicine);

        Intent i = getIntent();
        userId = i.getStringExtra("userId");

        Fragment fragment = new PrescribeMedicineFragment();
        FragmentManager fm = getSupportFragmentManager();
        Bundle data = new Bundle();
        data.putString("userId", userId);

        if(i.getStringExtra("ptnName") == null)
            Log.d(TAG, "onCreate: No Patient Name Found");
        else {

            Log.d(TAG, "onCreate: Patient Name found " + i.getStringExtra("ptnName"));
            data.putString("ptnName", i.getStringExtra("ptnName"));
        }
        if(i.getStringExtra("ptnAge") == null)
            Log.d(TAG, "onCreate: No Patient Age Found");
        else {
            Log.d(TAG, "onCreate: Patient Age found " + i.getStringExtra("ptnAge"));
            data.putString("ptnAge", i.getStringExtra("ptnAge"));
        }
        if(i.getStringExtra("ptnGender") == null)
            Log.d(TAG, "onCreate: No Patient Gender Found");
        else {
            Log.d(TAG, "onCreate: Patient Gender found " + i.getStringExtra("ptnGender"));
            data.putString("ptnGender", i.getStringExtra("ptnGender"));
        }

        fragment.setArguments(data);
        fm.beginTransaction().replace(R.id.prescribe_infalter, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}


