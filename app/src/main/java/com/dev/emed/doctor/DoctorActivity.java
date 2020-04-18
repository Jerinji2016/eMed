package com.dev.emed.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dev.emed.MainActivity;
import com.dev.emed.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DoctorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String doc_userid;

    private static final String TAG = "DoctorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Toolbar toolbar = findViewById(R.id.doc_toolbar);
        toolbar.setTitle(R.string.dashboard);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: Create Doc Activity");

        drawer = findViewById(R.id.doctor_dashboard_layout);

        NavigationView navigationView = findViewById(R.id.doc_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        doc_userid = intent.getStringExtra("userId");

        View headerView = navigationView.getHeaderView(0);
        final TextView navHeaderName = headerView.findViewById(R.id.nav_doc_name);
        final TextView navHeaderId = headerView.findViewById(R.id.nav_doc_reff_id);

        final DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Doctor");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    navHeaderName.setText(Objects.requireNonNull(dataSnapshot.child(doc_userid).child("name").getValue()).toString());
                    navHeaderId.setText(Objects.requireNonNull(dataSnapshot.child(doc_userid).child("member_ID").getValue()).toString());
                }
                else
                    reff.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("RF-NavSet", databaseError.toString());
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new DoctorProfileFragment();
            Bundle data = new Bundle();
            data.putString("userId", doc_userid);
            fragment.setArguments(data);
            fm.beginTransaction().replace(R.id.doc_fragment_container, fragment).commit();
            navigationView.setCheckedItem(R.id.doc_profile);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new DoctorProfileFragment();
        Bundle data = new Bundle();
        data.putString("userId", doc_userid);

        switch (item.getItemId()) {
            case R.id.doc_profile:
                fragment = new DoctorProfileFragment();
                break;
            case R.id.doc_prescription:
                fragment = new PrescribeMedicineFragment();
                break;
            case R.id.doc_ptn_history:
                fragment = new PatientHistoryFragment();
                break;
            case R.id.doc_med_prescribed:
                fragment = new PrescriptionHistoryFragment();
                break;
            case R.id.doc_logout:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
        fragment.setArguments(data);
        fm.beginTransaction().replace(R.id.doc_fragment_container, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
