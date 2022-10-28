package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.models.CategoryModel;

public class Category implements BaseModel {
    private int categoryId;
    private int hardwareStoreId;
    private String categoryName;

    public Category(){}
    public Category(CategoryModel categoryModel){
        categoryId = categoryModel.getcategoryId();
        hardwareStoreId = categoryModel.getHardwareStoreId();
        categoryName = categoryModel.getCategoryName();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
