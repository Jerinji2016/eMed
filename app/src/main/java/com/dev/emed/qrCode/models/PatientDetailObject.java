package com.dev.emed.qrCode.models;

public class PatientDetailObject {

    private String name;
    private String userId;
    private String reffId;
    private String age;
    private String gender;
    private String phone;

    private String weight;
    private String height;
    private String medConditions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReffId() {
        return reffId;
    }

    public void setReffId(String reffId) {
        this.reffId = reffId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMedConditions() {
        return medConditions;
    }

    public void setMedConditions(String medConditions) {
        this.medConditions = medConditions;
    }

    public PatientDetailObject() {
    }


}
