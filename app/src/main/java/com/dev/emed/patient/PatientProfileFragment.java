package com.dev.emed.patient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.emed.MainActivity;
import com.dev.emed.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "userId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userid Parameter 1.
     * @return A new instance of fragment PatientProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String user = getArguments().getString(ARG_PARAM1);

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
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        DatabaseReference mReff = FirebaseDatabase.getInstance().getReference("Patient");
        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ptnUser = dataSnapshot.child(user);

                ptnUserName.setText((String) ptnUser.child("name").getValue());
                ptnUserId.setText((String) ptnUser.child("user_name").getValue());
                ptnUserReffId.setText((String) ptnUser.child("member_ID").getValue());

                long age = (long) ptnUser.child("age").getValue();
                ptnUserAge.setText(Long.toString(age));
                ptnUserGender.setText((String) ptnUser.child("gender").getValue());

                long phoneNo = (long) ptnUser.child("phone_no").getValue();
                String phone = Long.toString(phoneNo);
                ptnUserPhone.setText(phone);

                ptnUserWeight.setText((String) ptnUser.child("physic").child("weight").getValue());
                ptnUserHeight.setText((String) ptnUser.child("physic").child("height").getValue());
                ptnUserMedConditions.setText((String) ptnUser.child("medicalConditions").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Ptn-Profile", databaseError.toString());
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
