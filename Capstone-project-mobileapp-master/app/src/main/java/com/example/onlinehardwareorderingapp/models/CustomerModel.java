package com.example.onlinehardwareorderingapp.models;

import java.util.Date;

public class CustomerModel {
    private int customerId;
    private String accountId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;
    private String contactNo;
    private int age;
    private String birthDate;
    private boolean isVerified;
    private double latitude;
    private double longitude;
    public CustomerModel(){}
    public CustomerModel(int customerId, String accountId, String firstName,
                         String lastName, String middleName, String address,
                         String contactNo, int age, String birthDate, boolean isVerified,
                         double latitude, double longitude){
        this.customerId = customerId;
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.address = address;
        this.contactNo = contactNo;
        this.age = age;
        this.birthDate = birthDate;
        this.isVerified = isVerified;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
