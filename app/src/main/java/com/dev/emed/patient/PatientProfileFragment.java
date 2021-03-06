package com.dev.emed.patient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dev.emed.MainActivity;
import com.dev.emed.R;
import com.dev.emed.models.PatientDetailObject;
import com.dev.emed.qrCode.OpenQrDialog;
import com.dev.emed.qrCode.helper.EncryptionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PatientProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "userId";
    private static final String TAG = "PatientProfileFragment";

    private PatientDetailObject myPtn;

    private DatabaseReference mReff;
    private ValueEventListener listener;

    public PatientProfileFragment() {
        // Required empty public constructor
    }

    public static PatientProfileFragment newInstance(String userid) {
        PatientProfileFragment fragment = new PatientProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String user = getArguments().getString(ARG_PARAM1);
        myPtn = new PatientDetailObject();

        View view = inflater.inflate(R.layout.ptn_fragment_patient_profile, container, false);
        final TextView ptnUserId = view.findViewById(R.id.ptn_profile_userid);
        final TextView ptnUserName = view.findViewById(R.id.ptn_profile_name);
        final TextView ptnUserReffId = view.findViewById(R.id.ptn_profile_reffid);
        final TextView ptnUserAge = view.findViewById(R.id.ptn_profile_age);
        final TextView ptnUserGender = view.findViewById(R.id.ptn_profile_gender);
        final TextView ptnUserPhone = view.findViewById(R.id.ptn_profile_phone);
        final TextView ptnUserWeight = view.findViewById(R.id.ptn_profile_weight);
        final TextView ptnUserHeight = view.findViewById(R.id.ptn_profile_height);
        final TextView ptnUserMedConditions = view.findViewById(R.id.ptn_profile_medical_conditions);
        final TextView ptnUserLastDoc = view.findViewById(R.id.ptn_profile_last_doc);
        final TextView ptnUserCurrentMed = view.findViewById(R.id.ptn_profile_current_medicine);

        ImageView logoutBtn = view.findViewById(R.id.ptn_profile_logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getActivity().getSharedPreferences("rememberLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("rememberMe", false);
                editor.apply();
                getActivity().finish();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button createQrBtn = view.findViewById(R.id.ptn_create_qr_btn);
        createQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Opening Dialog
                String str = new Gson().toJson(myPtn);
                String enc = EncryptionHelper.getInstance()
                        .encryptionString(str)
                        .encryptMsg();

                OpenQrDialog dialog = new OpenQrDialog();
                Bundle data = new Bundle();
                data.putString("enc_text", enc);
                dialog.setArguments(data);
                dialog.show(getFragmentManager(), "QR Code Patient Profile");
            }
        });

        mReff = FirebaseDatabase.getInstance().getReference("Patient");
        listener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ptnUser = dataSnapshot.child(user);

                ptnUserName.setText((String) ptnUser.child("name").getValue());
                myPtn.setName((String) ptnUser.child("name").getValue());

                ptnUserId.setText((String) ptnUser.child("user_name").getValue());
                myPtn.setUserId((String) ptnUser.child("user_name").getValue());

                ptnUserReffId.setText((String) ptnUser.child("member_ID").getValue());

                long age = (long) ptnUser.child("age").getValue();
                ptnUserAge.setText(Long.toString(age));

                ptnUserGender.setText((String) ptnUser.child("gender").getValue());

                long phoneNo = (long) ptnUser.child("phone_no").getValue();
                String phone = Long.toString(phoneNo);
                ptnUserPhone.setText(phone);

                ptnUserWeight.setText((String) ptnUser.child("physic").child("weight").getValue());
                ptnUserHeight.setText((String) ptnUser.child("physic").child("height").getValue());


                //  Fetch from medicalConditions
                DataSnapshot med = ptnUser.child("medicalConditions");
                StringBuilder condition = new StringBuilder();

                String prefix = "";
                if (med.child("textTyped").getValue() != null) {
                    for (DataSnapshot item : med.child("textTyped").getChildren()) {
                        if (((String) item.getValue()).contains("-")) {
                            String[] arr = ((String) item.getValue()).split("-");
                            condition.append(prefix).append(arr[0]).append("\n\t\t*\t").append(arr[1]);
                        } else {
                            condition.append(prefix).append(item.getValue());
                        }
                        prefix = "\n";
                    }
                }

                if (med.child(getString(R.string.carcinoma_cancer)).getValue() != null) {
                    condition.append(prefix).append("Carcinoma (Cancer) :");
                    for (DataSnapshot item : med.child(getString(R.string.carcinoma_cancer)).getChildren()) {
                        condition.append(prefix).append("\t\t - ").append(item.getValue());
                        prefix = "\n";
                    }
                }

                if (med.child(getString(R.string.tuberculosis)).getValue() != null) {
                    condition.append(prefix).append("Tuberculosis :");
                    for (DataSnapshot item : med.child(getString(R.string.tuberculosis)).getChildren()) {
                        condition.append(prefix).append("\t\t - ").append(item.getValue());
                        prefix = "\n";
                    }
                }

                if (condition.length() > 0)
                    ptnUserMedConditions.setText(condition.toString());
                else {
                    ptnUserMedConditions.setText("No Medical Conditions");
                    ptnUserMedConditions.setTypeface(ptnUserMedConditions.getTypeface(), Typeface.BOLD_ITALIC);
                }

                if (ptnUser.child("lastConsultedDoc").getValue() != null)
                    ptnUserLastDoc.setText((String) ptnUser.child("lastConsultedDoc").getValue());
                if (ptnUser.child("currentMed").getValue() != null) {
                    DataSnapshot data = ptnUser.child("currentMed");
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    List list = data.getValue(t);
                    Log.d(TAG, "\nonDataChange: " + data + "\n\n" + list);

                    String str = "";
                    for (Object s : list)
                        str += s + "\n";

                    ptnUserCurrentMed.setText(str);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };

        mReff.addValueEventListener(listener);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listener != null)
            mReff.removeEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listener != null)
            mReff.addValueEventListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listener != null)
            mReff.removeEventListener(listener);
    }
}
