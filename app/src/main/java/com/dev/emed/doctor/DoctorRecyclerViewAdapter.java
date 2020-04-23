package com.dev.emed.doctor;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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
        String pid = ((Item) holder).pId.getText().toString();
        pid += dataList.get(position).id;
        ((Item) holder).pId.setText(pid);

        ((Item) holder).pDate.setText(dataList.get(position).date);
        ((Item) holder).pName.setText(dataList.get(position).name);

        String details = dataList.get(position).gender + " - " + dataList.get(position).age + " y.o.";
        ((Item) holder).pDetails.setText(details);

        //  Add rows of medicine
        for (DataSnapshot item : dataList.get(position).prescription.getChildren()) {

            TableRow tr = new TableRow(context);
            tr.setPadding(0, 5, 0, 5);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tr.setWeightSum(2);
            tr.setGravity(Gravity.CENTER);

            TextView medName = new TextView(context);
            medName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            medName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            medName.setText((String) item.child("medName").getValue());

            TextView medIns = new TextView(context);
            medIns.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            medIns.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            String str = item.child("medDose").getValue() + " - ";
            str +=  item.child("medDur").getValue() == "0" ? "everyday" : item.child("medDur").getValue() + " days";
            str += "\n" + item.child("medFood").getValue() + " - " + item.child("medTime").getValue();
            medIns.setText(str);

            TextView space = new TextView(context);
            space.setLayoutParams(new TableRow.LayoutParams(2, TableRow.LayoutParams.MATCH_PARENT, 0f));
            space.setBackgroundColor(context.getResources().getColor(R.color.borderBlackColor));

            tr.addView(medName);
            tr.addView(space);
            tr.addView(medIns);

            ((Item)holder).medList.addView(tr);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class Item extends RecyclerView.ViewHolder {
        TextView pId, pDate, pName, pDetails;
        TableLayout medList;
        ImageButton qrCode;

        public Item(@NonNull View itemView) {
            super(itemView);

            pId = itemView.findViewById(R.id.prescription_id);
            pName = itemView.findViewById(R.id.prescription_ptn_name);
            pDate = itemView.findViewById(R.id.prescription_date);
            pDetails = itemView.findViewById(R.id.prescription_ptn_details);

            medList = itemView.findViewById(R.id.prescription_list);
            qrCode = itemView.findViewById(R.id.view_qr_btn);
        }
    }
}