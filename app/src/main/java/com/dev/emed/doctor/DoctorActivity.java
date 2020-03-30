package com.dev.emed.doctor;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.emed.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorActivity extends AppCompatActivity {
    TextView dName;
    TextView dSpecialisation;
    TextView dID;

    DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Patient");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        dName = findViewById(R.id.doc_name_text);
        dSpecialisation = findViewById(R.id.doc_specialisation_text);
        dID = findViewById(R.id.doc_reff_id_text);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("count", ""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase Error", ""+databaseError.getDetails());
            }
        });
    }
}
