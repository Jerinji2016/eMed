package com.dev.emed.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dev.emed.models.DocQrObject;
import com.dev.emed.qrCode.OpenQrDialog;
import com.dev.emed.qrCode.helper.EncryptionHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

public class DoctorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String doc_userid;

    private static final String TAG = "DoctorActivity";

    ValueEventListener listener;
    DatabaseReference reff;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Toolbar toolbar = findViewById(R.id.doc_toolbar);
        toolbar.setTitle(R.string.dashboard);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: Create Doc Activity");

        drawer = findViewById(R.id.doctor_dashboard_layout);
        navigationView = findViewById(R.id.doc_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        doc_userid = intent.getStringExtra("userId");

        View headerView = navigationView.getHeaderView(0);
        final TextView navHeaderName = headerView.findViewById(R.id.nav_doc_name);
        final TextView navHeaderId = headerView.findViewById(R.id.nav_doc_reff_id);

        reff = FirebaseDatabase.getInstance().getReference("Doctor").child(doc_userid);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                navHeaderName.setText(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                navHeaderId.setText(Objects.requireNonNull(dataSnapshot.child("member_ID").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("RF-NavSet", databaseError.getMessage());
            }
        };

        reff.addListenerForSingleValueEvent(listener);

        Log.d(TAG, "onCreate: Added Value Event Listener for Nav Head");

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
        } else {
            MenuItem item = navigationView.getCheckedItem();
            int id = item.getItemId();
            if(id == R.id.doc_profile)
                finish();
            else {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = new DoctorProfileFragment();
                Bundle data = new Bundle();
                data.putString("userId", doc_userid);
                fragment.setArguments(data);
                fm.beginTransaction().replace(R.id.doc_fragment_container, fragment).commit();
                navigationView.setCheckedItem(R.id.doc_profile);
            }
        }
    }

    Fragment fragment, docProfileFrag;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        fragment = new DoctorProfileFragment();
        Bundle data = new Bundle();
        data.putString("userId", doc_userid);

        switch (item.getItemId()) {
            case R.id.doc_profile:
                fragment = new DoctorProfileFragment();
                docProfileFrag = fragment;
                break;
            case R.id.doc_prescription:
                Intent j = new Intent(this, PrescribeMedicineActivity.class);
                j.putExtra("userId", doc_userid);
                startActivity(j);
                return true;
            case R.id.doc_ptn_history:
                fragment = new PatientHistoryFragment();
                break;
            case R.id.doc_logout:
                finishActivity();
                break;
        }
        fragment.setArguments(data);
        fm.beginTransaction().replace(R.id.doc_fragment_container, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showQrCode(View view) {
        View parent = (View) view.getParent().getParent();
        TextView idText = parent.findViewById(R.id.prescription_id);
        String pId = idText.getText().toString();

        DocQrObject qrData = new DocQrObject();
        qrData.setConsultId(pId);
        qrData.setDocUserId(doc_userid);

        String str = new Gson().toJson(qrData);
        String enc = EncryptionHelper.getInstance()
                .encryptionString(str)
                .encryptMsg();

        final OpenQrDialog dialog = new OpenQrDialog();
        Bundle data = new Bundle();
        data.putString("enc_text", enc);
        dialog.setArguments(data);

        dialog.show(getSupportFragmentManager(), "QR Code Doctor");
    }

    public void finishActivity() {
        SharedPreferences preferences = getSharedPreferences("rememberLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("rememberMe", false);
        editor.apply();

        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}























