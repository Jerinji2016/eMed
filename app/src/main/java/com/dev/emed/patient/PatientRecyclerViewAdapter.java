package com.dev.emed.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.emed.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PatientRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private DataSnapshot dataSnapshot;

    PatientRecyclerViewAdapter(Context context, DataSnapshot dataSnapshot) {
        this.context = context;
        this.dataSnapshot = dataSnapshot;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.ptn_recycler_item, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataSnapshot item = dataSnapshot.child(String.valueOf(position));
        String id = (String) ((Item)holder).pId.getText();
        id += (String) item.child("prescriptionId").getValue();
        ((Item)holder).pId.setText(id);

        GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
        List li = item.child("medList").getValue(gti);

        String medNames = "";
        Iterator<String> i = li.iterator();
        while(i.hasNext()) {
            medNames += i.next();
            if(i.hasNext())
                medNames += ", ";
        }

        String docName = (String) ((Item)holder).pDoc.getText();
        docName += (String) item.child("docName").getValue();
        ((Item)holder).pDoc.setText(docName);

        ((Item)holder).pName.setText(medNames);
        ((Item)holder).pDate.setText((String) item.child("prescriptionDate").getValue());
    }

    @Override
    public int getItemCount() {
        return (int) dataSnapshot.getChildrenCount();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView pId, pName, pDate, pDoc;
        public Item(@NotNull View itemView) {
            super(itemView);
            pId = itemView.findViewById(R.id.ptn_prescription_id);
            pName = itemView.findViewById(R.id.ptn_prescription_med_names);
            pDate = itemView.findViewById(R.id.ptn_prescription_date);
            pDoc = itemView.findViewById(R.id.ptn_prescription_doc);
        }
    }
}























