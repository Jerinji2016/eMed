package com.dev.emed.doctor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dev.emed.R;
import com.dev.emed.qrCode.OpenQrDialog;
import com.dev.emed.qrCode.helper.EncryptionHelper;
import com.dev.emed.models.DocQrObject;
import com.dev.emed.models.PrescriptionObject;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.primitives.Chars;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PrescribeMedicineFragment extends Fragment implements OnItemSelectedListener {
    private View view;
    private String docUserId;
    private static final String TAG = "PrescribeMedFragment";
    private boolean ptnAvailable;

    private EditText medNameInput;
    private EditText medDoseInput;
    private EditText medDurInput;
    private RadioGroup medMealInput;
    private Spinner medTimeInput;

    private EditText medPtnNameInput;
    private EditText medPtnAgeInput;
    private RadioGroup medPtnGenderInput;

    private ArrayList<PrescriptionObject> medPrescription = new ArrayList<>();
    private String medPtnName;
    private String medPtnAge;
    private String medPtnGender;
    private String consultId;

    private ArrayList<String> medArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.doc_fragment_prescribe_medicine, container, false);

        Bundle args = getArguments();
        docUserId = Objects.requireNonNull(args).getString("userId");

        if (args != null && !args.containsKey("ptnUserId")) {
            ptnAvailable = false;
            Toast.makeText(getActivity(), "Patient UserID Not Found", Toast.LENGTH_SHORT).show();
            view.findViewById(R.id.med_ptn_details).setVisibility(View.VISIBLE);
        } else {
            ptnAvailable = true;
            Toast.makeText(getActivity(), "Patient UserID Found", Toast.LENGTH_SHORT).show();
            view.findViewById(R.id.med_ptn_details).setVisibility(View.GONE);
        }

        medPtnNameInput = view.findViewById(R.id.med_ptn_name);
        medPtnAgeInput = view.findViewById(R.id.med_ptn_age);
        medPtnGenderInput = view.findViewById(R.id.med_ptn_gender);

        medNameInput = view.findViewById(R.id.med_name);
        medDoseInput = view.findViewById(R.id.med_dose);
        medDurInput = view.findViewById(R.id.med_duration);
        medMealInput = view.findViewById(R.id.med_food_radio);
        medTimeInput = view.findViewById(R.id.med_time_select);

        Button addMedicineBtn = view.findViewById(R.id.add_medicine_btn);
        Button shareMedQrBtn = view.findViewById(R.id.share_prescription_btn);

        shareMedQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (medPtnNameInput.getText().toString().trim().isEmpty() ||
                        medPtnAgeInput.getText().toString().trim().isEmpty() ||
                        medPtnGenderInput.getCheckedRadioButtonId() == -1) {
                    // Patient Details empty or no medicine
                    Snackbar snackbar = Snackbar.make(view, "Patient Details empty or No Medicine Added", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    medPtnName = medPtnNameInput.getText().toString().trim();
                    medPtnAge = medPtnAgeInput.getText().toString().trim();
                    RadioButton medPtnGenderRadio = view.findViewById(medPtnGenderInput.getCheckedRadioButtonId());
                    medPtnGender = medPtnGenderRadio.getText().toString();
                    consultId = idGenerator();

                    Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();

                    DocQrObject qrData = new DocQrObject();
                    qrData.setDocUserId(docUserId);
                    qrData.setConsultId(consultId);

                    String str = new Gson().toJson(qrData);
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    String enc = EncryptionHelper.getInstance()
                            .encryptionString(str)
                            .encryptMsg();

                    final OpenQrDialog dialog = new OpenQrDialog();
                    Bundle data = new Bundle();
                    data.putString("enc_text", enc);
                    dialog.setArguments(data);
                    dialog.setTargetFragment(PrescribeMedicineFragment.this, 1337);
                    dialog.show(getParentFragmentManager(), "QR Code Dialog");

                    Log.d(TAG, "onClick: Dialog Fragment Created");
                }
            }
        });

        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkEmpty()) {
                    if (!medArray.contains(medNameInput.getText().toString())) {
                        String medName = medNameInput.getText().toString().trim();
                        String medDoseDur = medDoseInput.getText().toString().trim() + "\n" + medDurInput.getText().toString().trim();
                        String medInstr = "", medTimeStr, medFoodStr;

                        medArray.add(medName);

                        medTimeStr = String.valueOf(medTimeInput.getSelectedItem());
                        RadioButton mealRadio = view.findViewById(medMealInput.getCheckedRadioButtonId());
                        medFoodStr = mealRadio.getText().toString();

                        medInstr = PrescriptionObject.timeFoodConverter(mealRadio.getText().toString(), medTimeStr);

                        medPrescription.add(new PrescriptionObject(medName,
                                medDoseInput.getText().toString().trim(),
                                medDurInput.getText().toString().trim(),
                                medTimeStr, medFoodStr));

                        TableLayout medListTarget = view.findViewById(R.id.medicine_list);
                        TableRow medList = new TableRow(view.getContext());
                        medList.setPadding(0, 5, 0, 5);

                        medList.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                        medList.setWeightSum(6);

                        TextView medNameText = new TextView(view.getContext());
                        TextView medDoseDurText = new TextView(view.getContext());
                        TextView medInstrText = new TextView(view.getContext());

                        medNameText.setText(medName);
                        medList.addView(medNameText);
                        medNameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        medNameText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 3f));

                        medDoseDurText.setText(medDoseDur);
                        medList.addView(medDoseDurText);
                        medDoseDurText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        medDoseDurText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));

                        medInstrText.setText(medInstr);
                        medList.addView(medInstrText);
                        medInstrText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        medInstrText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));

                        medListTarget.addView(medList);
                        Log.d(TAG, "onClick: Medicine Added to List");
                    } else {
                        Snackbar snackbar = Snackbar.make(view, "The Medicine is already Added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            }
        });

        return view;
    }

    private String idGenerator() {
        String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUWXYZ1234567890";
        List<Character> chars = Chars.asList(alpha.toCharArray());
        Collections.shuffle(chars);

        chars = chars.subList(0, 16);

        StringBuilder sb = new StringBuilder();
        for (Character ch : chars)
            sb.append(ch);

        String generatedID = sb.toString();
        return generatedID;
    }

    private boolean checkEmpty() {
        if (!ptnAvailable) {
            if (medPtnNameInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Patient name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (medPtnAgeInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Patient Age was not provided", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (medPtnGenderInput.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getActivity(), "Gender was not set", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (medNameInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Medicine Name cannot be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (medDoseInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Medicine Dose cannot be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (medDoseInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Medicine Duration cannot be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (medMealInput.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please Select Before or After Meal", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String medInstr = "";
        switch (String.valueOf(medTimeInput.getSelectedItem())) {
            case "OD":
                medInstr = "Once a day";
                break;
            case "BiD":
                medInstr = "Once every 12 hrs";
                break;
            case "TiD":
                medInstr = "Once every 8 hrs";
                break;
            case "QiD":
                medInstr = "Once every 6 hrs";
                break;
        }
        Toast.makeText(getActivity(), medInstr, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void dbUpdate() {
        //  Add values to Firebase

        DatabaseReference mReff = FirebaseDatabase.getInstance().getReference("Doctor").child(docUserId);
        int n = 0;
        for (PrescriptionObject obj : medPrescription) {
            Log.d(TAG, "Loop: " + obj);
            mReff.child("consultants").child(consultId).child("prescription").child(String.valueOf(n)).setValue(obj);
            n++;
        }

        SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        mReff.child("consultants").child(consultId).child("consultDate").setValue(date);
        mReff.child("consultants").child(consultId).child("ptnName").setValue(medPtnName);
        mReff.child("consultants").child(consultId).child("ptnAge").setValue(medPtnAge);
        mReff.child("consultants").child(consultId).child("ptnGender").setValue(medPtnGender);

        Toast.makeText(getActivity(), "Medicine Added to Prescription History", Toast.LENGTH_SHORT).show();
    }
}
