package com.dev.emed.patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.emed.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public PatientHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userid Parameter 1.
     * @return A new instance of fragment PatientHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ptn_fragment_patient_history, container, false);
        final TextView target = view.findViewById(R.id.target_text);
        String user = getArguments().getString(ARG_PARAM1);

        DatabaseReference mReff = FirebaseDatabase.getInstance().getReference("Patient").child(user);

        target.setText("");
        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot it : dataSnapshot.getChildren()) {
                    String temp = target.getText().toString();
                    temp += it.getKey() + " : ";
                    temp += it.getValue() + "\n";
                    target.setText(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}
