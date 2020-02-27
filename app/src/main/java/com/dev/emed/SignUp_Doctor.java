package com.dev.emed;

public class SignUp_Doctor {
    private String First_name;
    private String Last_name;
    private String Specialisation;
    private String Hospital_name;
    private String Hospital_address;
    private String Gender;
    private long Phone_no;

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        First_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(String specialisation) {
        Specialisation = specialisation;
    }

    public String getHospital_name() {
        return Hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        Hospital_name = hospital_name;
    }

    public String getHospital_address() {
        return Hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        Hospital_address = hospital_address;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public long getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(long phone_no) {
        Phone_no = phone_no;
    }

    public SignUp_Doctor() {
    }
}
