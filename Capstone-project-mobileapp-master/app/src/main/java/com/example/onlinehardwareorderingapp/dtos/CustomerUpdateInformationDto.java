package com.example.onlinehardwareorderingapp.dtos;

import com.example.onlinehardwareorderingapp.models.CustomerModel;

import java.util.Date;

public class CustomerUpdateInformationDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;
    private String contactNo;
    private int age;
    private String birthDate;
    private double lat;
    private double lng;

    public CustomerUpdateInformationDto(String firstName, String lastName, String middleName,
                                        String address, String contactNo, int
                                         age, String birthDate){
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.address = address;
        this.contactNo = contactNo;
        this.age = age;
        this.birthDate = birthDate;
    }
    public CustomerUpdateInformationDto(String firstName, String lastName, String middleName,
                                        String address, String contactNo, int
                                                age, String birthDate, double lat, double lng){
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.address = address;
        this.contactNo = contactNo;
        this.age = age;
        this.birthDate = birthDate;
        this.lat = lat;
        this.lng = lng;
    }
    public CustomerUpdateInformationDto(CustomerModel customerModel){
        this.firstName = customerModel.getFirstName();
        this.lastName = customerModel.getLastName();
        this.middleName = customerModel.getMiddleName();
        this.address = customerModel.getAddress();
        this.contactNo = customerModel.getContactNo();
        this.age = customerModel.getAge();
        this.birthDate = customerModel.getBirthDate();
        this.lat = customerModel.getLatitude();
        this.lng = customerModel.getLongitude();
    }
    public CustomerUpdateInformationDto(){}

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
