package com.dev.emed.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dev.emed.R;
import com.dev.emed.models.PatientDetailObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;

public class PatientDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PatientDetailsActivity";
    String myPtn;
    DataSnapshot ptnDetails;

    private String userId;

    DatabaseReference mReff;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        Log.d(TAG, "onCreate: PatientDetails");

        Intent i = getIntent();
        String decryptedString = i.getStringExtra("dcy_text");
        userId = i.getStringExtra("userId");

        Log.d(TAG, "onCreate: Decrypted String : "+decryptedString);

        PatientDetailObject patient = new Gson()
                .fromJson(decryptedString, PatientDetailObject.class);

        TextView nameText = findViewById(R.id.ptn_detail_name);
        TextView userIdText = findViewById(R.id.ptn_detail_userid);
        final TextView reffIdText = findViewById(R.id.ptn_detail_reffid);
        final TextView ageText = findViewById(R.id.ptn_detail_age);
        final TextView genderText = findViewById(R.id.ptn_detail_gender);
        final TextView weightText = findViewById(R.id.ptn_detail_weight);
        final TextView heightText = findViewById(R.id.ptn_detial_height);
        final TextView medConditionText = findViewById(R.id.ptn_detail_medCondition);

        nameText.setText(patient.getName());
        userIdText.setText(patient.getUserId());

        myPtn = patient.getUserId();
        Log.d(TAG, "onCreate: \nPatient Name -" + patient.getName() + "\nPatient Id - "+patient.getUserId());
        mReff = FirebaseDatabase.getInstance().getReference("Patient");
        listener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ptnDetails = dataSnapshot.child(myPtn);

                reffIdText.setText((String) ptnDetails.child("member_ID").getValue());

                long age = (long) ptnDetails.child("age").getValue();
                ageText.setText(Long.toString(age));
                genderText.setText((String) ptnDetails.child("gender").getValue());

                weightText.setText((String) ptnDetails.child("physic").child("weight").getValue());
                heightText.setText((String) ptnDetails.child("physic").child("height").getValue());
                medConditionText.setText((String) ptnDetails.child("medicalConditions").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };

        mReff.addValueEventListener(listener);

        Button showHistory = findViewById(R.id.show_ptn_med_history_btn);
        Button cancelPres = findViewById(R.id.cancel_prescription);
        Button createPrescription = findViewById(R.id.set_prescription);
        final LinearLayout medHistoryLayout = findViewById(R.id.ptn_detail_med_history_layout);

        createPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: User : "+userId);

                Intent i = new Intent(getApplicationContext(), PrescribeMedicineActivity.class);

                i.putExtra("userId", userId);
                i.putExtra("ptnName", (String) ptnDetails.child("name").getValue());

                long age = (long) ptnDetails.child("age").getValue();
                i.putExtra("ptnAge", Long.toString(age));
                i.putExtra("ptnGender", (String) ptnDetails.child("gender").getValue());

                startActivity(i);

//                Fragment fragment = new PrescribeMedicineFragment();
//                FragmentManager fm = getSupportFragmentManager();
//
//                Bundle data = new Bundle();
//                data.putString("userId", userId);
//                data.putString("ptnName", (String) ptnDetails.child("name").getValue());
//
//
//                data.putString("ptnAge", Long.toString(age));
//                data.putString("ptnGender", (String) ptnDetails.child("gender").getValue());
//                fragment.setArguments(data);
//                findViewById(R.id.ptn_detail_layout).setVisibility(View.GONE);
//                findViewById(R.id.prescribe_medicine_inflate_layout).setVisibility(View.VISIBLE);
//                fm.beginTransaction().replace(R.id.prescribe_medicine_inflate_layout, fragment).commit();
            }
        });

        cancelPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medHistoryLayout.setVisibility(View.VISIBLE);

                DataSnapshot medNameList = ptnDetails.child("medHistory");
                if (medNameList.getValue() != null) {
                    for (DataSnapshot item : medNameList.getChildren()) {
                        String DocName;
                        String MedName = "";
                        String Date;

                        DocName = (String) item.child("docName").getValue();
                        Date = (String) item.child("prescriptionDate").getValue();

                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                        };
                        List li = item.child("medList").getValue(t);

                        Iterator<String> it = li.iterator();
                        while(it.hasNext()) {
                            MedName += it.next();
                            if(it.hasNext())
                                MedName += "\n";
                        }
                        Log.d(TAG, "\nonClick: " + DocName + "\n" + MedName + "\n" + Date);

                        TableRow row = new TableRow(getApplicationContext());
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.setWeightSum(7);
                        row.setGravity(Gravity.CENTER);

                        TableRow rowLine = new TableRow(getApplicationContext());
                        rowLine.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 2, 1f));
                        rowLine.setBackground(getResources().getDrawable(R.drawable.btn_primary_default));

                        TextView pMedNames = new TextView(getApplicationContext());
                        TextView pDocName = new TextView(getApplicationContext());
                        TextView pDate = new TextView(getApplicationContext());
                        TextView space1 = new TextView(getApplicationContext());
                        TextView space2 = new TextView(getApplicationContext());

                        space1.setLayoutParams(new TableRow.LayoutParams(2, TableRow.LayoutParams.MATCH_PARENT, 0f));
                        space1.setBackgroundColor(getResources().getColor(R.color.borderBlackColor));
                        space2.setLayoutParams(new TableRow.LayoutParams(2, TableRow.LayoutParams.MATCH_PARENT, 0f));
                        space2.setBackgroundColor(getResources().getColor(R.color.borderBlackColor));

                        pMedNames.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                        pMedNames.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        pMedNames.setPadding(0, 12, 0, 13);
                        pMedNames.setText(MedName);
                        row.addView(pMedNames);
                        row.addView(space1);

                        pDocName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                        pDocName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        pDocName.setPadding(0, 12, 0, 13);
                        pDocName.setText(DocName);
                        row.addView(pDocName);
                        row.addView(space2);

                        pDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                        pDate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        pDate.setPadding(0, 12, 0, 13);
                        pDate.setText(Date);
                        row.addView(pDate);

                        medHistoryLayout.addView(row);
                        medHistoryLayout.addView(rowLine);
                    }
                }
                else
                    findViewById(R.id.no_entries_found_msg).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReff.removeEventListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReff.removeEventListener(listener);
    }
}






















