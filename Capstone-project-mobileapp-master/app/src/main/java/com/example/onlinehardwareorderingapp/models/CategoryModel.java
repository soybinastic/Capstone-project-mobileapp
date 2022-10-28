package com.example.onlinehardwareorderingapp.models;

public class CategoryModel {
    private int categoryId;
    private int hardwareStoreId;
    private String categoryName;
    public CategoryModel(int categoryId, int hardwareStoreId, String name){
        this.categoryId = categoryId;
        this.hardwareStoreId = hardwareStoreId;
        this.categoryName = name;
    }
    public int getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public void setHardwareStoreId(int hardwareStoreId) {
        this.hardwareStoreId = hardwareStoreId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }
}
