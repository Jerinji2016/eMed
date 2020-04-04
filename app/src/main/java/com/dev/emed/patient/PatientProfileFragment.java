package com.dev.emed.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.emed.R;

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
        String user = getArguments().getString(ARG_PARAM1);

        View view = inflater.inflate(R.layout.ptn_fragment_patient_profile, container, false);
        TextView t = view.findViewById(R.id.username);
        t.setText(user);
        // Inflate the layout for this fragment
        return view;
    }
}
