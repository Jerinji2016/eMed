package com.dev.emed.patient;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.emed.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientHistoryFragment extends Fragment {

    private static final String TAG = "PatientHistoryFragment";
    private static final String ARG_PARAM1 = "param1";
    private String userId;

    RecyclerView recyclerView;

    DatabaseReference mReff;
    ValueEventListener mListener;

    public PatientHistoryFragment() {
        // Required empty public constructor
    }

    public static PatientHistoryFragment newInstance(String userid) {
        PatientHistoryFragment fragment = new PatientHistoryFragment();
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
        View view = inflater.inflate(R.layout.ptn_fragment_patient_history, container, false);
        if (getArguments() != null)
            userId = getArguments().getString(ARG_PARAM1);

        //  RecyclerView happens
        recyclerView = view.findViewById(R.id.ptn_med_history_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mReff = FirebaseDatabase.getInstance().getReference("Patient").child(userId);
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot medHistorySnapshot = dataSnapshot.child("medHistory");
                    recyclerView.setAdapter(new PatientRecyclerViewAdapter(getActivity(), medHistorySnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        };
        // Inflate the layout for this fragment
        mReff.addValueEventListener(mListener);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null) {
            mReff.removeEventListener(mListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mReff.removeEventListener(mListener);
        }
    }
}
