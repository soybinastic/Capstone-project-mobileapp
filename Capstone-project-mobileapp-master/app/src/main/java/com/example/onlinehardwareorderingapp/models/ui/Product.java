package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.models.ProductModel;

public class Product implements BaseModel {
    private int id;
    private int hardwareProductId;
    private int hardwareStoreId;
    private int branchId;
    private int categoryId;
    private String description;
    private String brand;
    private String quality;
    private int stockNumber;
    private boolean isAvailable;
    private boolean isAvailableInWarehouse;
    private String name;
    private double price;
    private String imageFile;

    public Product(ProductModel productModel){
        id = productModel.getId();
        hardwareProductId = productModel.getHardwareProductId();
        hardwareStoreId = productModel.getHardwareStoreId();
        branchId = productModel.getBranchId();
        categoryId = productModel.getCategoryId();
        description = productModel.getDescription();
        brand = productModel.getBrand();
        quality = productModel.getQuality();
        stockNumber = productModel.getStockNumber();
        isAvailable = productModel.isAvailable();
        isAvailableInWarehouse = productModel.isAvailableInWarehouse();
        name = productModel.getName();
        price = productModel.getPrice();
        imageFile = productModel.getImageFile();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHardwareProductId() {
        return hardwareProductId;
    }

    public void setHardwareProductId(int hardwareProductId) {
        this.hardwareProductId = hardwareProductId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isAvailableInWarehouse() {
        return isAvailableInWarehouse;
    }

    public void setAvailableInWarehouse(boolean availableInWarehouse) {
        isAvailableInWarehouse = availableInWarehouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
