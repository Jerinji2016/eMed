package com.dev.emed.patient;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dev.emed.R;
import com.dev.emed.models.OnSwipeListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class PatientExtrasFragment extends Fragment implements View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "PatientExtrasFragment";

    private int bHr, lHr, dHr;
    private int bMin, lMin, dMin;

    private TextView bfTime;
    private TextView lunchTime;
    private TextView dinnerTime;

    private FoodTimings ptnFoodTiming;

    private int currentContainerId;
    private int currentSystemOpBtnId;
    private View view;
    private String userId;

    private ArrayList<String> medCondition = new ArrayList<>();
    private List<String> tuberculosisTypes = new ArrayList<>();
    private List<String> carcinomaTypes = new ArrayList<>();

    private DatabaseReference mReff;
    private ValueEventListener fListener;

    public PatientExtrasFragment() {
        // Required empty public constructor
    }

    private static class FoodTimings {
        int bh = -1;
        int bm = -1;
        int lh = -1;
        int lm = -1;
        int dh = -1;
        int dm = -1;

        void setBh(int bh) {
            this.bh = bh;
        }

        void setBm(int bm) {
            this.bm = bm;
        }

        void setLh(int lh) {
            this.lh = lh;
        }

        void setLm(int lm) {
            this.lm = lm;
        }

        void setDh(int dh) {
            this.dh = dh;
        }

        void setDm(int dm) {
            this.dm = dm;
        }
    }

    public static PatientExtrasFragment newInstance(String userid) {
        PatientExtrasFragment fragment = new PatientExtrasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
            userId = getArguments().getString(ARG_PARAM1);
        view = inflater.inflate(R.layout.ptn_fragment_patient_extras, container, false);

        currentContainerId = R.id.op1_central_nervous_option_container;
        currentSystemOpBtnId = R.id.system_op1;

        ptnFoodTiming = new FoodTimings();

        view.findViewById(R.id.system_op1).setOnClickListener(this);
        view.findViewById(R.id.system_op2).setOnClickListener(this);
        view.findViewById(R.id.system_op3).setOnClickListener(this);
        view.findViewById(R.id.system_op4).setOnClickListener(this);
        view.findViewById(R.id.system_op5).setOnClickListener(this);
        view.findViewById(R.id.system_op6).setOnClickListener(this);
        view.findViewById(R.id.system_op7).setOnClickListener(this);
        view.findViewById(R.id.system_op8).setOnClickListener(this);
        view.findViewById(R.id.system_op9).setOnClickListener(this);

        view.findViewById(R.id.op2_diabetes).setOnClickListener(this);
        view.findViewById(R.id.op2_thyroid_disorder).setOnClickListener(this);
        view.findViewById(R.id.op3_copd).setOnClickListener(this);
        view.findViewById(R.id.op4_ccf).setOnClickListener(this);
        view.findViewById(R.id.op4_bp).setOnClickListener(this);
        view.findViewById(R.id.op7_kidney_failure).setOnClickListener(this);
        view.findViewById(R.id.op9_hepatitis).setOnClickListener(this);
        view.findViewById(R.id.op9_tuberculosis).setOnClickListener(this);
        view.findViewById(R.id.op9_carcinoma).setOnClickListener(this);

        view.findViewById(R.id.op1_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op2_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op3_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op4_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op5_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op6_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op7_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op8_next_btn).setOnClickListener(this);
        view.findViewById(R.id.op9_next_btn).setOnClickListener(this);

        view.findViewById(R.id.add_tuberculosis_types_btn).setOnClickListener(this);
        view.findViewById(R.id.add_carcinoma_types_btn).setOnClickListener(this);

        bfTime = view.findViewById(R.id.breakfast_time);
        lunchTime = view.findViewById(R.id.lunch_time);
        dinnerTime = view.findViewById(R.id.dinner_time);

        mReff = FirebaseDatabase.getInstance().getReference("Patient").child(userId);
        setValuesToAllViews();

        bfTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ptnFoodTiming.setBh(hourOfDay);
                        ptnFoodTiming.setBm(minute);
                        String time = updateTime(ptnFoodTiming.bh, ptnFoodTiming.bm);

                        bfTime.setText(time);
                    }
                };

                new TimePickerDialog(getActivity(), timePickerListener, bHr, bMin, false).show();
            }
        });

        lunchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ptnFoodTiming.setLh(hourOfDay);
                        ptnFoodTiming.setLm(minute);
                        String time = updateTime(ptnFoodTiming.lh, ptnFoodTiming.lm);

                        lunchTime.setText(time);
                    }
                };

                new TimePickerDialog(getActivity(), timePickerListener, lHr, lMin, false).show();
            }
        });

        dinnerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ptnFoodTiming.setDh(hourOfDay);
                        ptnFoodTiming.setDm(minute);
                        String time = updateTime(ptnFoodTiming.dh, ptnFoodTiming.dm);

                        dinnerTime.setText(time);
                    }
                };

                new TimePickerDialog(getActivity(), timePickerListener, dHr, dMin, false).show();
            }
        });

        return view;
    }

    private void setValuesToAllViews() {
        final int hr, min;
        Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        fListener = new ValueEventListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ((EditText) view.findViewById(R.id.ptn_weight)).setText((String) dataSnapshot.child("physic").child("weight").getValue());
                ((EditText) view.findViewById(R.id.ptn_height)).setText((String) dataSnapshot.child("physic").child("height").getValue());

                if (dataSnapshot.child("foodTimings").child("breakfast").getValue() != null) {
                    int a = (int) ((long) dataSnapshot.child("foodTimings").child("breakfast").child("hour").getValue());
                    int b = (int) ((long) dataSnapshot.child("foodTimings").child("breakfast").child("min").getValue());
                    ((TextView) view.findViewById(R.id.breakfast_time)).setText(updateTime(a, b));
                    bHr = a;
                    bMin = b;
                } else {
                    bHr = hr;
                    bMin = min;
                }

                if (dataSnapshot.child("foodTimings").child("lunch").getValue() != null) {
                    int a = (int) ((long) dataSnapshot.child("foodTimings").child("lunch").child("hour").getValue());
                    int b = (int) ((long) dataSnapshot.child("foodTimings").child("lunch").child("min").getValue());
                    ((TextView) view.findViewById(R.id.lunch_time)).setText(updateTime(a, b));
                    lHr = a;
                    lMin = b;
                } else {
                    lHr = hr;
                    lMin = min;
                }

                if (dataSnapshot.child("foodTimings").child("dinner").getValue() != null) {
                    int a = (int) ((long) dataSnapshot.child("foodTimings").child("dinner").child("hour").getValue());
                    int b = (int) ((long) dataSnapshot.child("foodTimings").child("dinner").child("min").getValue());
                    ((TextView) view.findViewById(R.id.dinner_time)).setText(updateTime(a, b));
                    dHr = a;
                    dMin = b;
                } else {
                    dHr = hr;
                    dMin = min;
                }

                dataSnapshot = dataSnapshot.child("medicalConditions");

                if (dataSnapshot.child(getString(R.string.tuberculosis)).getValue() != null) {
                    ((CheckBox) view.findViewById(R.id.op9_tuberculosis)).setChecked(true);
                    view.findViewById(R.id.op9_tuberculosis_sub).setVisibility(View.VISIBLE);
                    final LinearLayout tb = view.findViewById(R.id.tuberculosis_types_container);
                    tb.removeAllViewsInLayout();
                    for (final DataSnapshot item : dataSnapshot.child(getString(R.string.tuberculosis)).getChildren()) {
                        final TextView tbType = new TextView(getContext());

                        tbType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        tbType.setPadding(0, 5, 0, 5);
                        tbType.setText((String) item.getValue());
                        tb.addView(tbType);
                        tuberculosisTypes.add((String) item.getValue());

                        tbType.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                tb.removeView(tbType);
                                tuberculosisTypes.remove(item.getValue());
                                return true;
                            }
                        });
                    }
                }

                if (dataSnapshot.child(getString(R.string.carcinoma_cancer)).getValue() != null) {
                    ((CheckBox) view.findViewById(R.id.op9_carcinoma)).setChecked(true);
                    view.findViewById(R.id.op9_carcinoma_sub).setVisibility(View.VISIBLE);
                    final LinearLayout cc = view.findViewById(R.id.carcinoma_type_container);
                    cc.removeAllViewsInLayout();
                    for (final DataSnapshot item : dataSnapshot.child(getString(R.string.carcinoma_cancer)).getChildren()) {
                        final TextView ccType = new TextView(getContext());
                        ccType.setPadding(0, 5, 0, 5);
                        ccType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        ccType.setText((String) item.getValue());
                        cc.addView(ccType);
                        carcinomaTypes.add((String) item.getValue());

                        ccType.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                cc.removeView(ccType);
                                carcinomaTypes.remove(item.getValue());
                                return true;
                            }
                        });
                    }
                }

                if (dataSnapshot.child("textTyped").getValue() != null) {
                    for (DataSnapshot item : dataSnapshot.child("textTyped").getChildren()) {
                        String dName = (String) item.getValue();
                        String op = "";

                        if (dName.contains("-")) {
                            String[] arr = dName.split("-");
                            dName = arr[0];
                            op = arr[1];
                        }

                        Log.d(TAG, "onDataChange: " + dName + "-" + op);
                        switch (dName) {
                            // Op1 container
                            case "Stroke":
                                ((CheckBox) view.findViewById(R.id.op1_stroke)).setChecked(true);
                                break;
                            case "Brain Tumor":
                                ((CheckBox) view.findViewById(R.id.op1_brain_tumor)).setChecked(true);
                                break;
                            case "Meningitis":
                                ((CheckBox) view.findViewById(R.id.op1_meningitis)).setChecked(true);
                                break;

                            // Op2 container
                            case "Diabetes":
                                ((CheckBox) view.findViewById(R.id.op2_diabetes)).setChecked(true);
                                view.findViewById(R.id.op2_diabetes_sub).setVisibility(View.VISIBLE);
                                if ((op.equals("Type 1")))
                                    ((RadioButton) view.findViewById(R.id.type_1_diabetes)).setChecked(true);
                                else if ((op.equals("Type 2")))
                                    ((RadioButton) view.findViewById(R.id.type_2_diabetes)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.type_1_diabetes)).setChecked(true);
                                break;
                            case "Thyroid Disorder":
                                ((CheckBox) view.findViewById(R.id.op2_thyroid_disorder)).setChecked(true);
                                view.findViewById(R.id.op2_thyroid_disorder_sub).setVisibility(View.VISIBLE);
                                if ((op.equals("Hyperthyroidism")))
                                    ((RadioButton) view.findViewById(R.id.hyperthyroidism)).setChecked(true);
                                else if ((op.equals("Hypothyroidism")))
                                    ((RadioButton) view.findViewById(R.id.hyperthyroidism)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.hyperthyroidism)).setChecked(true);
                                break;

                            // Op3 container
                            case "COPD (Chronic Obstructive Pulmonary Diease)":
                                ((CheckBox) view.findViewById(R.id.op3_copd)).setChecked(true);
                                view.findViewById(R.id.op3_copd_sub).setVisibility(View.VISIBLE);
                                if (op.equals("Chronic Bronchitis"))
                                    ((RadioButton) view.findViewById(R.id.chronic_bronchitis)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.emphysema)).setChecked(true);
                                break;
                            case "Asthma":
                                ((CheckBox) view.findViewById(R.id.op3_asthma)).setChecked(true);
                                break;
                            case "Pneumonia":
                                ((CheckBox) view.findViewById(R.id.op3_pnuemonia)).setChecked(true);
                                break;

                            // Op4 container
                            case "Congestive Cardiac Failure":
                                ((CheckBox) view.findViewById(R.id.op4_ccf)).setChecked(true);
                                view.findViewById(R.id.op4_ccf_sub).setVisibility(View.VISIBLE);
                                if (op.equals("Systolic Failure"))
                                    ((RadioButton) view.findViewById(R.id.systolic_failure)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.diastolic_failure)).setChecked(true);
                                break;
                            case "Angine":
                                ((CheckBox) view.findViewById(R.id.op4_angine)).setChecked(true);
                                break;
                            case "Blood Pressure":
                                ((CheckBox) view.findViewById(R.id.op4_bp)).setChecked(true);
                                view.findViewById(R.id.op4_bp_sub).setVisibility(View.VISIBLE);
                                if (op.equals("Hypertension"))
                                    ((RadioButton) view.findViewById(R.id.hypertension)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.hypotension)).setChecked(true);
                                break;
                            case "Vasculitis/Aneurysms":
                                ((CheckBox) view.findViewById(R.id.op4_vasulitis)).setChecked(true);
                                break;

                            // Op5 container
                            case "Cirrhosis":
                                ((CheckBox) view.findViewById(R.id.op5_cirrhosis)).setChecked(true);
                                break;
                            case "Pancreatitis":
                                ((CheckBox) view.findViewById(R.id.op5_pancreatitis)).setChecked(true);
                                break;
                            case "Peptic Ulcer":
                                ((CheckBox) view.findViewById(R.id.op5_peptic_ulcer)).setChecked(true);
                                break;

                            // Op6 container
                            case "PCOS (Polycystic Ovary Syndrome)":
                                ((CheckBox) view.findViewById(R.id.op6_pcos)).setChecked(true);
                                break;
                            case "Ovarian Cysts":
                                ((CheckBox) view.findViewById(R.id.op6_ovarian_cysts)).setChecked(true);
                                break;
                            case "Infertility":
                                ((CheckBox) view.findViewById(R.id.op6_infertility)).setChecked(true);
                                break;

                            // Op7 container
                            case "Kidney Failure":
                                ((CheckBox) view.findViewById(R.id.op7_kidney_failure)).setChecked(true);
                                view.findViewById(R.id.op7_kidney_failure_sub).setVisibility(View.VISIBLE);
                                if (op.equals("Acute Renal Failure"))
                                    ((RadioButton) view.findViewById(R.id.acute_renal_failure)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.chronic_renal_failure)).setChecked(true);
                                break;
                            case "Kidney Stone":
                                ((CheckBox) view.findViewById(R.id.op7_kidney_stone)).setChecked(true);
                                break;

                            // Op8 container
                            case "Osteoporosis":
                                ((CheckBox) view.findViewById(R.id.op8_osteoporosis)).setChecked(true);
                                break;
                            case "Arthritis":
                                ((CheckBox) view.findViewById(R.id.op8_arthiritis)).setChecked(true);
                                break;

                            // Op9 container
                            case "HIV":
                                ((CheckBox) view.findViewById(R.id.op9_hiv)).setChecked(true);
                                break;
                            case "Hepatitis":
                                ((CheckBox) view.findViewById(R.id.op9_hepatitis)).setChecked(true);
                                view.findViewById(R.id.op9_hepatitis_sub).setVisibility(View.VISIBLE);
                                if (op.equals("A"))
                                    ((RadioButton) view.findViewById(R.id.hep_a)).setChecked(true);
                                else if (op.equals("B"))
                                    ((RadioButton) view.findViewById(R.id.hep_b)).setChecked(true);
                                else if (op.equals("C"))
                                    ((RadioButton) view.findViewById(R.id.hep_c)).setChecked(true);
                                else if (op.equals("D"))
                                    ((RadioButton) view.findViewById(R.id.hep_d)).setChecked(true);
                                else
                                    ((RadioButton) view.findViewById(R.id.hep_e)).setChecked(true);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        };

        mReff.addValueEventListener(fListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fListener != null)
            mReff.removeEventListener(fListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fListener != null)
            mReff.addValueEventListener(fListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fListener != null)
            mReff.removeEventListener(fListener);
    }

    private String updateTime(int hours, int mins) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String time = String.valueOf(hours) + ':' + minutes + " " + timeSet;
        return time;
    }

    @Override
    public void onClick(View v) {

        if (String.valueOf(v.getClass()).equals("class androidx.appcompat.widget.AppCompatButton")) {
            switch (v.getId()) {
                case R.id.op1_next_btn:
                    view.findViewById(R.id.op1_central_nervous_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op1).setRotation(0);
                    view.findViewById(R.id.op2_endocrine_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op2).setRotation(180);
                    break;
                case R.id.op2_next_btn:
                    view.findViewById(R.id.op2_endocrine_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op2).setRotation(0);
                    view.findViewById(R.id.op3_respiratory_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op3).setRotation(180);
                    break;
                case R.id.op3_next_btn:
                    view.findViewById(R.id.op3_respiratory_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op3).setRotation(0);
                    view.findViewById(R.id.op4_cardiovascular_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op4).setRotation(180);
                    break;
                case R.id.op4_next_btn:
                    view.findViewById(R.id.op4_cardiovascular_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op4).setRotation(0);
                    view.findViewById(R.id.op5_alimentary_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op5).setRotation(180);
                    break;
                case R.id.op5_next_btn:
                    view.findViewById(R.id.op5_alimentary_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op5).setRotation(0);
                    view.findViewById(R.id.op6_reproductive_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op6).setRotation(180);
                    break;
                case R.id.op6_next_btn:
                    view.findViewById(R.id.op6_reproductive_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op6).setRotation(0);
                    view.findViewById(R.id.op7_urinary_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op7).setRotation(180);
                    break;
                case R.id.op7_next_btn:
                    view.findViewById(R.id.op7_urinary_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op7).setRotation(0);
                    view.findViewById(R.id.op8_skeletal_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op8).setRotation(180);
                    break;
                case R.id.op8_next_btn:
                    view.findViewById(R.id.op8_skeletal_option_container).setVisibility(View.GONE);
                    view.findViewById(R.id.system_op8).setRotation(0);
                    view.findViewById(R.id.op9_other_option_container).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.system_op9).setRotation(180);
                    break;
                case R.id.op9_next_btn:
                    updatePtnDB();
                    break;
                case R.id.add_tuberculosis_types_btn:
                    final LinearLayout tb = view.findViewById(R.id.tuberculosis_types_container);
                    EditText tbAdd = view.findViewById(R.id.tb_text);
                    final String tbAddText = tbAdd.getText().toString().trim();
                    if (!tuberculosisTypes.contains(tbAddText)) {
                        //  Add Tb type to the linearout
                        final TextView tbType = new TextView(getContext());
                        tbType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        tbType.setPadding(0, 5, 0, 5);
                        tbType.setText(tbAddText);
                        tb.addView(tbType);
                        tuberculosisTypes.add(tbAddText);
                        tbType.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                tb.removeView(tbType);
                                tuberculosisTypes.remove(tbAddText);
                                return true;
                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(view, tbAddText + " already added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    tbAdd.setText("");
                    break;
                case R.id.add_carcinoma_types_btn:
                    final LinearLayout cc = view.findViewById(R.id.carcinoma_type_container);
                    EditText ccAdd = view.findViewById(R.id.cc_text);
                    final String ccAddText = ccAdd.getText().toString().trim();
                    if (!carcinomaTypes.contains(ccAddText)) {
                        final TextView ccType = new TextView(getContext());
                        ccType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                        ccType.setPadding(0, 5, 0, 5);
                        ccType.setText(ccAddText);
                        cc.addView(ccType);
                        carcinomaTypes.add(ccAddText);
                        ccType.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                cc.removeView(ccType);
                                tuberculosisTypes.remove(ccAddText);
                                return true;
                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(view, ccAddText + " already added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    ccAdd.setText("");
                    break;
            }
        } else if (String.valueOf(v.getClass()).equals("class androidx.appcompat.widget.AppCompatCheckBox")) {
            Toast.makeText(getActivity(), "Checkbox", Toast.LENGTH_SHORT).show();
            CheckBox c = view.findViewById(v.getId());
            int visibility;
            if (c.isChecked())
                visibility = View.VISIBLE;
            else
                visibility = View.GONE;

            switch (v.getId()) {
                case R.id.op2_diabetes:
                    view.findViewById(R.id.op2_diabetes_sub).setVisibility(visibility);
                    break;
                case R.id.op2_thyroid_disorder:
                    view.findViewById(R.id.op2_thyroid_disorder_sub).setVisibility(visibility);
                    break;
                case R.id.op3_copd:
                    view.findViewById(R.id.op3_copd_sub).setVisibility(visibility);
                    break;
                case R.id.op4_ccf:
                    view.findViewById(R.id.op4_ccf_sub).setVisibility(visibility);
                    break;
                case R.id.op4_bp:
                    view.findViewById(R.id.op4_bp_sub).setVisibility(visibility);
                    break;
                case R.id.op7_kidney_failure:
                    view.findViewById(R.id.op7_kidney_failure_sub).setVisibility(visibility);
                    break;
                case R.id.op9_hepatitis:
                    view.findViewById(R.id.op9_hepatitis_sub).setVisibility(visibility);
                    break;
                case R.id.op9_tuberculosis:
                    view.findViewById(R.id.op9_tuberculosis_sub).setVisibility(visibility);
                    break;
                case R.id.op9_carcinoma:
                    view.findViewById(R.id.op9_carcinoma_sub).setVisibility(visibility);
                    break;
            }
        } else {
            Log.d("Ele", String.valueOf(v.getContext()));
            if (v.getId() == currentSystemOpBtnId) {
                view.findViewById(currentContainerId).setVisibility(View.GONE);
                view.findViewById(currentSystemOpBtnId).setRotation(0);
                currentSystemOpBtnId = 0;
                currentContainerId = 0;
            } else {
                if (currentContainerId != 0 && currentSystemOpBtnId != 0) {
                    view.findViewById(currentContainerId).setVisibility(View.GONE);
                    view.findViewById(currentSystemOpBtnId).setRotation(0);
                    currentSystemOpBtnId = 0;
                    currentContainerId = 0;
                }
                switch (v.getId()) {
                    case R.id.system_op1:
                        view.findViewById(R.id.op1_central_nervous_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op1).setRotation(180);
                        currentContainerId = R.id.op1_central_nervous_option_container;
                        currentSystemOpBtnId = R.id.system_op1;
                        break;
                    case R.id.system_op2:
                        view.findViewById(R.id.op2_endocrine_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op2).setRotation(180);
                        currentContainerId = R.id.op2_endocrine_option_container;
                        currentSystemOpBtnId = R.id.system_op2;
                        break;
                    case R.id.system_op3:
                        view.findViewById(R.id.op3_respiratory_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op3).setRotation(180);
                        currentContainerId = R.id.op3_respiratory_option_container;
                        currentSystemOpBtnId = R.id.system_op3;
                        break;
                    case R.id.system_op4:
                        view.findViewById(R.id.op4_cardiovascular_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op4).setRotation(180);
                        currentContainerId = R.id.op4_cardiovascular_option_container;
                        currentSystemOpBtnId = R.id.system_op4;
                        break;
                    case R.id.system_op5:
                        view.findViewById(R.id.op5_alimentary_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op5).setRotation(180);
                        currentContainerId = R.id.op5_alimentary_option_container;
                        currentSystemOpBtnId = R.id.system_op5;
                        break;
                    case R.id.system_op6:
                        view.findViewById(R.id.op6_reproductive_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op6).setRotation(180);
                        currentContainerId = R.id.op6_reproductive_option_container;
                        currentSystemOpBtnId = R.id.system_op6;
                        break;
                    case R.id.system_op7:
                        view.findViewById(R.id.op7_urinary_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op7).setRotation(180);
                        currentContainerId = R.id.op7_urinary_option_container;
                        currentSystemOpBtnId = R.id.system_op7;
                        break;
                    case R.id.system_op8:
                        view.findViewById(R.id.op8_skeletal_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op8).setRotation(180);
                        currentContainerId = R.id.op8_skeletal_option_container;
                        currentSystemOpBtnId = R.id.system_op8;
                        break;
                    case R.id.system_op9:
                        view.findViewById(R.id.op9_other_option_container).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.system_op9).setRotation(180);
                        currentContainerId = R.id.op9_other_option_container;
                        currentSystemOpBtnId = R.id.system_op9;
                        break;
                }
            }
        }
        currentContainerId = view.findViewById(R.id.op1_central_nervous_option_container).getVisibility() == View.VISIBLE ? R.id.op1_central_nervous_option_container :
                view.findViewById(R.id.op2_endocrine_option_container).getVisibility() == View.VISIBLE ? R.id.op2_endocrine_option_container :
                        view.findViewById(R.id.op3_respiratory_option_container).getVisibility() == View.VISIBLE ? R.id.op3_respiratory_option_container :
                                view.findViewById(R.id.op4_cardiovascular_option_container).getVisibility() == View.VISIBLE ? R.id.op4_cardiovascular_option_container :
                                        view.findViewById(R.id.op5_alimentary_option_container).getVisibility() == View.VISIBLE ? R.id.op5_alimentary_option_container :
                                                view.findViewById(R.id.op6_reproductive_option_container).getVisibility() == View.VISIBLE ? R.id.op6_reproductive_option_container :
                                                        view.findViewById(R.id.op7_urinary_option_container).getVisibility() == View.VISIBLE ? R.id.op7_urinary_option_container :
                                                                view.findViewById(R.id.op8_skeletal_option_container).getVisibility() == View.VISIBLE ? R.id.op8_skeletal_option_container :
                                                                        view.findViewById(R.id.op9_other_option_container).getVisibility() == View.VISIBLE ? R.id.op9_other_option_container : 0;

        currentSystemOpBtnId = view.findViewById(R.id.system_op1).getRotation() == 180 ? R.id.system_op1 :
                view.findViewById(R.id.system_op2).getRotation() == 180 ? R.id.system_op2 :
                        view.findViewById(R.id.system_op3).getRotation() == 180 ? R.id.system_op3 :
                                view.findViewById(R.id.system_op4).getRotation() == 180 ? R.id.system_op4 :
                                        view.findViewById(R.id.system_op5).getRotation() == 180 ? R.id.system_op5 :
                                                view.findViewById(R.id.system_op6).getRotation() == 180 ? R.id.system_op6 :
                                                        view.findViewById(R.id.system_op7).getRotation() == 180 ? R.id.system_op7 :
                                                                view.findViewById(R.id.system_op8).getRotation() == 180 ? R.id.system_op8 :
                                                                        view.findViewById(R.id.system_op9).getRotation() == 180 ? R.id.system_op9 : 0;
    }

    private void updatePtnDB() {
        medCondition.clear();

        //  Container 1 | CENTRAL NERVOUS SYSTEM
        simplyCheckBox(R.id.op1_stroke);
        simplyCheckBox(R.id.op1_brain_tumor);
        simplyCheckBox(R.id.op1_meningitis);

        //  Container 2 | ENDOCRINE SYSTEM
        checkBoxWithRadio(R.id.op2_diabetes, R.id.op2_diabetes_sub);
        checkBoxWithRadio(R.id.op2_thyroid_disorder, R.id.op2_thyroid_disorder_sub);

        //  Container 3 | RESPIRATORY SYSTEM
        checkBoxWithRadio(R.id.op3_copd, R.id.op3_copd_sub);
        simplyCheckBox(R.id.op3_asthma);
        simplyCheckBox(R.id.op3_pnuemonia);

        //  Container 4 | CADIOVASCULAR SYSTEM
        checkBoxWithRadio(R.id.op4_ccf, R.id.op4_ccf_sub);
        checkBoxWithRadio(R.id.op4_bp, R.id.op4_bp_sub);
        simplyCheckBox(R.id.op4_angine);
        simplyCheckBox(R.id.op4_vasulitis);

        //  Container 5 | ALIMENTARY SYSTEM
        simplyCheckBox(R.id.op5_cirrhosis);
        simplyCheckBox(R.id.op5_pancreatitis);
        simplyCheckBox(R.id.op5_peptic_ulcer);

        //  Container 6 | REPRODUCTIVE SYSTEM
        simplyCheckBox(R.id.op6_pcos);
        simplyCheckBox(R.id.op6_ovarian_cysts);
        simplyCheckBox(R.id.op6_infertility);

        //  Container 7 | URIARY SYSTEM
        checkBoxWithRadio(R.id.op7_kidney_failure, R.id.op7_kidney_failure_sub);
        simplyCheckBox(R.id.op7_kidney_stone);

        //  Container 8 | SKELETAL SYSTEM
        simplyCheckBox(R.id.op8_arthiritis);
        simplyCheckBox(R.id.op8_osteoporosis);

        //  Container 9 | OTHERS
        simplyCheckBox(R.id.op9_hiv);
        checkBoxWithRadio(R.id.op9_hepatitis, R.id.op9_hepatitis_sub);

        Map<String, Object> medUpdates = new HashMap<>();

        EditText weightBox = view.findViewById(R.id.ptn_weight);
        EditText heightBox = view.findViewById(R.id.ptn_height);

        mReff.child("medicalConditions").removeValue();
        mReff.child("medicalConditions").child("textTyped").setValue(medCondition);

        CheckBox checkbox = view.findViewById(R.id.op9_tuberculosis);
        if (checkbox.isChecked()) {
            if (!tuberculosisTypes.isEmpty())
                mReff.child("medicalConditions").child(getString(R.string.tuberculosis)).setValue(tuberculosisTypes);
            else {
                Snackbar snackbar = Snackbar.make(view, "Please input the type of Tuberculosis", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        checkbox = view.findViewById(R.id.op9_carcinoma);
        if (checkbox.isChecked()) {
            if (!carcinomaTypes.isEmpty())
                mReff.child("medicalConditions").child(getString(R.string.carcinoma_cancer)).setValue(carcinomaTypes);
            else {
                Snackbar snackbar = Snackbar.make(view, "Please input the type of Cancer", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        if (!weightBox.getText().toString().isEmpty())
            medUpdates.put("physic/weight", weightBox.getText().toString());
        if (!heightBox.getText().toString().isEmpty())
            medUpdates.put("physic/height", heightBox.getText().toString());

        if (ptnFoodTiming.bh > -1 && ptnFoodTiming.bm > -1) {
            medUpdates.put("foodTimings/breakfast/hour", ptnFoodTiming.bh);
            medUpdates.put("foodTimings/breakfast/min", ptnFoodTiming.bm);
        }
        if (ptnFoodTiming.lh > -1 && ptnFoodTiming.lm > -1) {
            medUpdates.put("foodTimings/lunch/hour", ptnFoodTiming.lh);
            medUpdates.put("foodTimings/lunch/min", ptnFoodTiming.lm);
        }
        if (ptnFoodTiming.bh > -1 && ptnFoodTiming.bm > -1) {
            medUpdates.put("foodTimings/dinner/hour", ptnFoodTiming.dh);
            medUpdates.put("foodTimings/dinner/min", ptnFoodTiming.dm);
        }

        mReff.updateChildren(medUpdates);
    }

    private void simplyCheckBox(int checkBoxId) {
        CheckBox checkBox = view.findViewById(checkBoxId);
        if (checkBox.isChecked())
            medCondition.add(checkBox.getText().toString());
    }

    private void checkBoxWithRadio(int checkBoxId, int radioGroupId) {
        CheckBox checkbox = view.findViewById(checkBoxId);
        if (checkbox.isChecked()) {
            RadioGroup radio = view.findViewById(radioGroupId);
            if (radio.getCheckedRadioButtonId() != -1) {
                RadioButton rad = view.findViewById(radio.getCheckedRadioButtonId());
                medCondition.add(checkbox.getText().toString() + "-" + rad.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Radio Problem", Toast.LENGTH_SHORT).show();
                Log.d("Radio", "Radio Error");
            }
        }
    }
}















