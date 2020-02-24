package com.dev.emed;

public class SignUp_Member {
    private String First_name;
    private String Last_name;
    private String User_name;
    private String Password;
    private String Email;
    private int Age;
    private long Phone_no;
    private String Member_ID;

    public String getMember_ID() {
        return Member_ID;
    }

    public void setMember_ID(String member_ID) {
        Member_ID = member_ID;
    }

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

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public long getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(long phone_no) {
        Phone_no = phone_no;
    }

    public SignUp_Member() {
    }
}
