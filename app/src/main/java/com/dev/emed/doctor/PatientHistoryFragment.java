package com.dev.emed.doctor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private String userId;
    private RecyclerView recyclerView;
    private DatabaseReference mReff;
    private ValueEventListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doc_fragment_patient_history, container, false);

        if(getArguments() != null)
            userId = getArguments().getString("userId");

        recyclerView = view.findViewById(R.id.doc_prescription_history_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mReff = FirebaseDatabase.getInstance().getReference("Doctor").child(userId);
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child("consultants");
                recyclerView.setAdapter(new DoctorRecyclerViewAdapter(getActivity(), ds));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        };

        mReff.addValueEventListener(mListener);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mListener != null)
            mReff.removeEventListener(mListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mListener != null)
            mReff.removeEventListener(mListener);
    }
}























