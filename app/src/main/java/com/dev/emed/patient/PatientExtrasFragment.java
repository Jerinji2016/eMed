package com.dev.emed.patient;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dev.emed.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientExtrasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientExtrasFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private int currentContainerId;
    private int currentSystemOpBtnId;
    private View view;
    private String currentConditions = "";
    private String user;
    private List<String> tuberculosisTypes = new ArrayList<>();
    private List<String> carcinomaTypes = new ArrayList<>();

    public PatientExtrasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userid Parameter 1.
     * @return A new instance of fragment PatientScan.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = getArguments().getString(ARG_PARAM1);
        view = inflater.inflate(R.layout.ptn_fragment_patient_extras, container, false);

        currentContainerId = R.id.op1_central_nervous_option_container;
        currentSystemOpBtnId = R.id.system_op1;

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

        // Inflate the layout for this fragment
        return view;
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
                    TextView tb = view.findViewById(R.id.tuberculosis_types_container);
                    String tbStr = tb.getText().toString();
                    EditText tbAdd = view.findViewById(R.id.tb_text);
                    String tbAddText = tbAdd.getText().toString().trim();
                    if (!tuberculosisTypes.contains(tbAddText)) {
                        tbStr += tbAddText + "\n";
                        tuberculosisTypes.add(tbAddText);
                        tb.setText(tbStr);
                    } else {
                        Snackbar snackbar = Snackbar.make(view, tbAddText + " already added", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    tbAdd.setText("");
                    break;
                case R.id.add_carcinoma_types_btn:
                    TextView cc = view.findViewById(R.id.carcinoma_type_container);
                    String ccStr = cc.getText().toString();
                    EditText ccAdd = view.findViewById(R.id.cc_text);
                    String ccAddText = ccAdd.getText().toString().trim();
                    if (!carcinomaTypes.contains(ccAddText)) {
                        ccStr += ccAddText + "\n";
                        carcinomaTypes.add(ccAddText);
                        cc.setText(ccStr);
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
        currentConditions = "";
        boolean updateFlag = true;

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

        CheckBox checkbox = view.findViewById(R.id.op9_tuberculosis);
        if (checkbox.isChecked()) {
            if (!tuberculosisTypes.isEmpty()) {
                currentConditions += "Tuberculosis - \n";
                for (String tuberculosisType : tuberculosisTypes)
                    currentConditions += "\t" + tuberculosisType + "\n";
                updateFlag = true;
            } else {
                Snackbar snackbar = Snackbar.make(view, "Please input the type of Tuberculosis", Snackbar.LENGTH_LONG);
                snackbar.show();
                updateFlag = false;
            }
        }

        checkbox = view.findViewById(R.id.op9_carcinoma);
        if (checkbox.isChecked()) {
            if (!carcinomaTypes.isEmpty()) {
                currentConditions += "Carcinoma (Cancer) - \n";
                for (String carcinomaType : carcinomaTypes)
                    currentConditions += "\t" + carcinomaType + "\n";
            } else {
                Snackbar snackbar = Snackbar.make(view, "Please input the type of Cancer", Snackbar.LENGTH_LONG);
                snackbar.show();
                updateFlag = false;
            }
        }
        EditText weightBox = view.findViewById(R.id.ptn_weight);
        EditText heightBox = view.findViewById(R.id.ptn_height);

        if (updateFlag) {
            Toast.makeText(getActivity(), currentConditions, Toast.LENGTH_SHORT).show();
            final DatabaseReference mReff = FirebaseDatabase.getInstance().getReference("Patient").child(user);
            Map<String, Object> medUpdate = new HashMap<>();
            medUpdate.put("medicalConditions", currentConditions);
            if (!weightBox.getText().toString().isEmpty())
                medUpdate.put("physic/weight", weightBox.getText().toString());
            if (!heightBox.getText().toString().isEmpty())
                medUpdate.put("physic/height", heightBox.getText().toString());
            mReff.updateChildren(medUpdate);
        }
    }

    private void simplyCheckBox(int checkBoxId) {
        CheckBox checkBox = view.findViewById(checkBoxId);
        currentConditions += checkBox.isChecked() ? checkBox.getText().toString() + "\n" : "";
    }

    private void checkBoxWithRadio(int checkBoxId, int radioGroupId) {
        CheckBox checkbox = view.findViewById(checkBoxId);
        if (checkbox.isChecked()) {
            RadioGroup radio = view.findViewById(radioGroupId);
            if (radio.getCheckedRadioButtonId() != -1) {
                RadioButton rad = view.findViewById(radio.getCheckedRadioButtonId());
                currentConditions += checkbox.getText().toString() + " - " + rad.getText().toString() + "\n";
            } else {
                Toast.makeText(getActivity(), "Radio Problem", Toast.LENGTH_SHORT).show();
                Log.d("Radio", "Radio Error");
            }
        }
    }
}