package com.example.onlinehardwareorderingapp.dtos.order;

import com.example.onlinehardwareorderingapp.models.CartItemModel;

public class ItemInCartDto {
    private int cartId;
    private int branchId;
    private int hardwareStoreId;
    private int customerId;
    private int productId;
    private int categoryId;
    private String productName;
    private String productDescription;
    private String productBrand;
    private String productQuality;
    private double productPrice;
    private int productQuantity;

    public ItemInCartDto(CartItemModel cartItemModel){
        cartId = cartItemModel.getCartId();
        branchId = cartItemModel.getBranchId();
        hardwareStoreId = cartItemModel.getHardwareStoreId();
        customerId = cartItemModel.getCustomerId();
        productId = cartItemModel.getProductId();
        categoryId = cartItemModel.getCategoryId();
        productName = cartItemModel.getProductName();
        productDescription = cartItemModel.getProductDescription();
        productBrand = cartItemModel.getProductBrand();
        productQuality = cartItemModel.getProductQuality();
        productPrice = cartItemModel.getProductPrice();
        productQuantity = cartItemModel.getProductQuantity();
    }
    public ItemInCartDto(){}

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public void setHardwareStoreId(int hardwareStoreId) {
        this.hardwareStoreId = hardwareStoreId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
