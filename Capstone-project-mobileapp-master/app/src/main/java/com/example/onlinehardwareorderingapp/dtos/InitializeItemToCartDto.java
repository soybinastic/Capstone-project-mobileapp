package com.example.onlinehardwareorderingapp.dtos;

public class InitializeItemToCartDto {
    private int hardwareStoreId;
    private int branchId;
    private int productId;
    private int categoryId;
    private String productName;
    private double productPrice;
    private String productDescription;
    private String productBrand;
    private String productQuality;

    public InitializeItemToCartDto(){}
    public InitializeItemToCartDto(int hardwareStoreId, int branchId, int productId, int categoryId,
                                   String productName, double productPrice, String productDescription,
                                   String productBrand, String productQuality){
        this.hardwareStoreId = hardwareStoreId;
        this.branchId = branchId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productBrand = productBrand;
        this.productQuality = productQuality;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductQuality() {
        return productQuality;
    }

    public void setProductQuality(String productQuality) {
        this.productQuality = productQuality;
    }
}
