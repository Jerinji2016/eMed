package com.dev.emed.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dev.emed.R;
import com.dev.emed.patient.ui.main.SectionsPagerAdapter;
import com.dev.emed.qrCode.QrCodeScanner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class PatientActivity extends AppCompatActivity {

    String ptn_userid;
    private static final String TAG = "PatientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent = getIntent();
        ptn_userid = intent.getStringExtra("userId");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.setUserId(ptn_userid);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientActivity.this, QrCodeScanner.class);
                i.putExtra("userType", "Patient");
                i.putExtra("userId", ptn_userid);
                startActivity(i);
            }
        });
    }
}