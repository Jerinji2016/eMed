package com.dev.emed.models;

import java.util.ArrayList;

public class PatientMedUpdateObject {
    public String docName, docId, prescriptionId, prescriptionDate;
    public ArrayList<String> medList;

    public PatientMedUpdateObject(String docName, String docId, String prescriptionId, String prescriptionDate, ArrayList<String> medList) {
        this.docName = docName;
        this.docId = docId;
        this.prescriptionId = prescriptionId;
        this.prescriptionDate = prescriptionDate;
        this.medList = new ArrayList<>(medList);
    }
}
