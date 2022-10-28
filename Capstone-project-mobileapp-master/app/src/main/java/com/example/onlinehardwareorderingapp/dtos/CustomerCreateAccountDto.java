package com.example.onlinehardwareorderingapp.dtos;

public class CustomerCreateAccountDto {
    private String firstName;
    private String lastName;
    private String address;
    private String contactNo;
    private String userName;
    private String password;
    private String confirmPassword;

    public CustomerCreateAccountDto(String firstName, String lastName, String address, String contactNo, String userName, String password, String confirmPassword){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNo = contactNo;
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    public CustomerCreateAccountDto(){}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
