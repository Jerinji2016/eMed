package com.dev.emed.patient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.emed.R;
import com.dev.emed.qrCode.models.DocQrObject;
import com.dev.emed.qrCode.models.PrescriptionObject;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Iterator;
import java.util.Objects;

public class PrescriptionDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PrescriptionDtlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        Log.d(TAG, "onCreate: Prescription Page");

        Intent i = getIntent();
        String decryptedString = i.getStringExtra("dcy_text");

        DocQrObject userObj = new Gson()
                .fromJson(decryptedString, DocQrObject.class);

        final String docId, prescriptionId;
        docId = userObj.getDocUserId();
        prescriptionId = userObj.getConsultId();

        Toast.makeText(this, docId + " & " + prescriptionId, Toast.LENGTH_SHORT).show();

        final TextView pId = findViewById(R.id.doc_prescription_ptn_id),
                pName = findViewById(R.id.doc_prescription_ptn_name),
                pAge = findViewById(R.id.doc_prescription_ptn_age),
                pGender = findViewById(R.id.doc_prescription_ptn_gender),
                pDocName = findViewById(R.id.doc_prescription_docName);

        final TableLayout targetTable = findViewById(R.id.prescription_medicine_list);

        Button closeBtn = findViewById(R.id.prescription_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        DatabaseReference mReff = FirebaseDatabase.getInstance().getReference("Doctor").child(docId);
        mReff.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "\nonDataChange: User "+dataSnapshot.child("name"));
                pDocName.setText(dataSnapshot.child("name").getValue()+"\n"+dataSnapshot.child("user_name").getValue());

                dataSnapshot = dataSnapshot.child("consultants").child(prescriptionId);

                pId.setText(prescriptionId);
                pName.setText(Objects.requireNonNull(dataSnapshot.child("ptnName").getValue()).toString());
                pAge.setText(Objects.requireNonNull(dataSnapshot.child("ptnAge").getValue()).toString());
                pGender.setText(Objects.requireNonNull(dataSnapshot.child("ptnGender").getValue()).toString());

                for (DataSnapshot item : dataSnapshot.child("prescription").getChildren()) {
                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setWeightSum(6);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                    TextView pMedName = new TextView(getApplicationContext());
                    TextView pMedDose = new TextView(getApplicationContext());
                    TextView pMedInst = new TextView(getApplicationContext());
                    TextView space1 = new TextView(getApplicationContext());
                    TextView space2 = new TextView(getApplicationContext());

                    space1.setLayoutParams(new TableRow.LayoutParams(2, TableRow.LayoutParams.MATCH_PARENT, 0f));
                    space1.setBackgroundColor(getResources().getColor(R.color.borderBlacklackColor));
                    space2.setLayoutParams(new TableRow.LayoutParams(2, TableRow.LayoutParams.MATCH_PARENT, 0f));
                    space2.setBackgroundColor(getResources().getColor(R.color.borderBlacklackColor));

                    PrescriptionObject pObj = new PrescriptionObject(
                            Objects.requireNonNull(item.child("medName").getValue()).toString(),
                            Objects.requireNonNull(item.child("medDose").getValue()).toString(),
                            Objects.requireNonNull(item.child("medDur").getValue()).toString(),
                            Objects.requireNonNull(item.child("medTime").getValue()).toString(),
                            Objects.requireNonNull(item.child("medFood").getValue()).toString());

                    Log.d(TAG, "onDataChange: "+pObj.medName+ " "+pObj.medDose+ " "+pObj.medDur+ " "+pObj.medFood+ " "+pObj.medTime+ " ");

                    pMedName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                    pMedName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    pMedName.setText(pObj.medName);
                    pMedName.setPadding(0, 10, 0, 10);
                    tr.addView(pMedName);
                    tr.addView(space1);

                    pMedDose.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    pMedDose.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    pMedDose.setText(pObj.medDose+"\n"+pObj.medDur);
                    pMedDose.setPadding(0, 10, 0, 10);
                    tr.addView(pMedDose);
                    tr.addView(space2);

                    pMedInst.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                    pMedInst.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    pMedInst.setText(PrescriptionObject.timeFoodConverter(pObj.medFood, pObj.medTime));
                    pMedInst.setPadding(0, 10, 0, 10);
                    tr.addView(pMedInst);

                    targetTable.addView(tr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}





















