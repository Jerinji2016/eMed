package com.dev.emed.patient;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.emed.R;
import com.dev.emed.models.MedicineReminder;
import com.dev.emed.models.PatientMedUpdateObject;
import com.dev.emed.models.PrescriptionObject;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class PrescriptionDetailsActivity extends AppCompatActivity {
    private static final String TAG = "PrescriptionDtlActivity";
    private String userId;
    private String docId;
    private String prescriptionId;
    private String docName;
    final ArrayList<String> medList = new ArrayList<>();

    DatabaseReference docReff, ptnReff;

    AlarmManager alarmManager;
    DataSnapshot reminderTime;

    MedicineReminder medicineReminder;
    ArrayList<PrescriptionObject> medObj = new ArrayList<>();

    long n;
    ValueEventListener docValueEventListener, ptnValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        Intent i = getIntent();

//        String decryptedString = i.getStringExtra("dcy_text");
        userId = i.getStringExtra("userId");
//        DocQrObject userObj = new Gson()
//                .fromJson(decryptedString, DocQrObject.class);

//        docId = userObj.getDocUserId();
//        prescriptionId = userObj.getConsultId();

        docId = "subinannan";
        prescriptionId = "e1or6IKiF8kDRqnW";


        final TextView pId = findViewById(R.id.doc_prescription_ptn_id);
        final TextView pName = findViewById(R.id.doc_prescription_ptn_name);
        final TextView pAge = findViewById(R.id.doc_prescription_ptn_age);
        final TextView pGender = findViewById(R.id.doc_prescription_ptn_gender);
        final TextView pDocName = findViewById(R.id.doc_prescription_docName);

        final TableLayout targetTable = findViewById(R.id.prescription_medicine_list);

        Button closeBtn = findViewById(R.id.prescription_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        docReff = FirebaseDatabase.getInstance().getReference("Doctor").child(docId);
        docValueEventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "\nonDataChange: User " + dataSnapshot.child("name"));
                docName = (String) dataSnapshot.child("name").getValue();
                Log.d(TAG, "onDataChange: " + docName + " or " + dataSnapshot.child("name").getValue());
                pDocName.setText(docName + "\n" + docId);

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
                    space1.setBackgroundColor(getResources().getColor(R.color.borderBlackColor));
                    space2.setLayoutParams(new TableRow.LayoutParams(2, TableRow.LayoutParams.MATCH_PARENT, 0f));
                    space2.setBackgroundColor(getResources().getColor(R.color.borderBlackColor));

                    PrescriptionObject pObj = new PrescriptionObject(
                            (String) item.child("medName").getValue(),
                            (String) item.child("medDose").getValue(),
                            (String) item.child("medDur").getValue(),
                            (String) item.child("medTime").getValue(),
                            (String) item.child("medFood").getValue());

                    medObj.add(pObj);

                    pMedName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                    pMedName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    pMedName.setPadding(0, 10, 0, 10);
                    pMedName.setText(pObj.medName);
                    tr.addView(pMedName);
                    tr.addView(space1);

                    medList.add(pObj.medName);

                    pMedDose.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    pMedDose.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    pMedDose.setText(pObj.medDose + "\n" + ((pObj.medDur.equals("0")) ? "everyday" : pObj.medDur + " days"));
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

                //  Add Details to Patient DB
                SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                final PatientMedUpdateObject ptnMed = new PatientMedUpdateObject(docName, docId, prescriptionId, date, medList);

                ptnReff = FirebaseDatabase.getInstance().getReference("Patient").child(userId);

                //  Check count of medHistory and then add newer med values
                ptnReff.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        reminderTime = dataSnapshot.child("foodTimings");

                        n = dataSnapshot.child("medHistory").getChildrenCount();

//                        ptnReff.child("lastConsultedDoc").setValue(docName);
//                        ptnReff.child("currentMed").setValue(medList);
//                        ptnReff = ptnReff.child("medHistory");
//                        ptnReff.child(Long.toString(n)).setValue(ptnMed);

                        Toast.makeText(getApplicationContext(), "Medicines Added to Ptn DB", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };

        docReff.addValueEventListener(docValueEventListener);

        Switch setReminderBtn = findViewById(R.id.prescription_reminder_switch);
        setReminderBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (reminderTime.getValue() != null) {

                        medicineReminder = new MedicineReminder(medObj, reminderTime, getApplicationContext(), findViewById(R.id.prescription_details_layout));
                        medicineReminder.setReminder();
                    } else {
                        Snackbar snackbar = Snackbar.make(
                                findViewById(R.id.prescription_details_layout),
                                "Reminder was not set since your Food Timing are incomplete",
                                Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    medicineReminder.removeReminder();
                    Toast.makeText(PrescriptionDetailsActivity.this, "Reminder Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (docValueEventListener != null)
            docReff.removeEventListener(docValueEventListener);
        if (ptnValueEventListener != null)
            ptnReff.removeEventListener(ptnValueEventListener);
    }
}