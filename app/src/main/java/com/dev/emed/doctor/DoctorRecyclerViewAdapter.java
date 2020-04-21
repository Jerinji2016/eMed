package com.dev.emed.doctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.emed.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

class DataList {
    public String id;
    public String name;
    public String age;
    public String gender;
    public String date;
    public DataSnapshot prescription;

    public DataList(String id, String name, String age, String gender, String date, DataSnapshot prescription) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.date = date;
        this.prescription = prescription;
    }
}

public class DoctorRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<DataList> dataList = new ArrayList<>();

    DoctorRecyclerViewAdapter(Context context, DataSnapshot dataSnapshot) {
        this.context = context;

        for (DataSnapshot item : dataSnapshot.getChildren()) {
            Log.d(TAG, "DoctorRecyclerViewAdapter: " + item.child("ptnName").toString());

            this.dataList.add(new DataList(item.getKey(),
                    (String) item.child("ptnName").getValue(),
                    (String) item.child("ptnAge").getValue(),
                    (String) item.child("ptnGender").getValue(),
                    (String) item.child("consultDate").getValue(),
                    item.child("prescription")));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.doc_recycler_item, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Item) holder).pId.setText(dataList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView pId, pDate, pName, pDetails;
        TableLayout medList;

        public Item(@NonNull View itemView) {
            super(itemView);

            pId = itemView.findViewById(R.id.prescription_id);
            pName = itemView.findViewById(R.id.prescription_date);
            pDate = itemView.findViewById(R.id.prescription_ptn_name);
            pDetails = itemView.findViewById(R.id.prescription_ptn_details);

            medList = itemView.findViewById(R.id.prescription_list);
        }
    }
}
