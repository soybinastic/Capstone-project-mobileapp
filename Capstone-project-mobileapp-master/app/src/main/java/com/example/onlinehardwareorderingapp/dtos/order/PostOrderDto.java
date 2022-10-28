package com.example.onlinehardwareorderingapp.dtos.order;

import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.models.CustomerModel;

import java.util.ArrayList;

public class PostOrderDto {
    private int hardwareStoreId;
    private int branchId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String contactNo;
    private boolean deliver;
    private ArrayList<ItemInCartDto> products;
    private String latitude;
    private String longtitude;
    private double shippingFee;

    public PostOrderDto(){}
    public PostOrderDto(CustomerModel customer, ArrayList<CartItemModel> cartItems,
                        String lat, String lon, boolean isDeliver,
                        int branchId, int hardwareStoreId, double shippingFee){
        this.branchId = branchId;
        this.hardwareStoreId = hardwareStoreId;
        firstName = customer.getFirstName();
        lastName = customer.getLastName();
        address = customer.getAddress();
        email = "customer@gmail.com";
        contactNo = customer.getContactNo();
        longtitude = lon;
        latitude = lat;
        deliver = isDeliver;
        this.shippingFee = shippingFee;

        products = new ArrayList<>();

        for (CartItemModel item : cartItems){
            products.add(new ItemInCartDto(item));
        }
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public void setHardwareStoreId(int hardwareStoreId) {
        this.hardwareStoreId = hardwareStoreId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
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

    public boolean isDeliver() {
        return deliver;
    }

    public void setDeliver(boolean deliver) {
        this.deliver = deliver;
    }

    public ArrayList<ItemInCartDto> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ItemInCartDto> products) {
        this.products = products;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }
}
