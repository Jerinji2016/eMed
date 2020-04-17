package com.dev.emed.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dev.emed.MainActivity;
import com.dev.emed.R;
import com.dev.emed.qrCode.QrCodeScanner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DoctorProfileFragment extends Fragment {
    private View view;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doc_fragment_profile, container, false);

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Doctor");
        userId = Objects.requireNonNull(getArguments()).getString("userId");

        ImageView logout = view.findViewById(R.id.logout_btn);
        Button docScanBtn = view.findViewById(R.id.doc_scan_btn);

        docScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QrCodeScanner.class);
                i.putExtra("userType", "Doctor");
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView docName = view.findViewById(R.id.doc_name_text);
                TextView docSpecialisation = view.findViewById(R.id.doc_specialisation_text);
                TextView docId = view.findViewById(R.id.doc_reff_id_text);

//                docName.setText(Objects.requireNonNull(dataSnapshot.child(userId).child("name").getValue()).toString());
//                docSpecialisation.setText(Objects.requireNonNull(dataSnapshot.child(userId).child("specialisation").getValue()).toString());
//                docId.setText(Objects.requireNonNull(dataSnapshot.child(userId).child("member_ID").getValue()).toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("RF - SetProfile", databaseError.toString());
            }
        });

        return view;
    }


}
