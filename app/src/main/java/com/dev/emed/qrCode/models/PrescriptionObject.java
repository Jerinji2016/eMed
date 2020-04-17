package com.dev.emed.qrCode.models;

public class PrescriptionObject {
    public String medName;
    public String medDose;
    public String medDur;
    public String medTime;
    public String medFood;

    public PrescriptionObject(String medName, String medDose, String medDur, String medTime, String medFood) {
        this.medName = medName;
        this.medDose = medDose;
        this.medDur = medDur;
        this.medTime = medTime;
        this.medFood = medFood;
    }

    public static String timeFoodConverter(String foodMed, String timeMed) {

        String medInstr = "";

        switch (timeMed) {
            case "OD":
                medInstr = "Once a day,\n";
                break;
            case "BiD":
                medInstr = "Once every 12 hrs,\n";
                break;
            case "TiD":
                medInstr = "Once every 8 hrs,\n";
                break;
            case "QiD":
                medInstr = "Once every 6 hrs,\n";
                break;
        }

        if (foodMed.equals("AC"))
            medInstr += "Before Food";
        else
            medInstr += "After Food";

        return medInstr;
    }
}

