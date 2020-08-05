package com.example.ecommerceapp;

public class ProductDataStructure {
    private String mNameProduct;
    private String mPriceProduct;
    private int mQuantityProduct;

    public ProductDataStructure(String name, String price, int quantity) {
        mNameProduct = name;
        mPriceProduct = price;
        mQuantityProduct = quantity;
    }

    public String getNameProduct() {
        return mNameProduct;
    }

    public void setNameProduct(String mNameProduct) {
        this.mNameProduct = mNameProduct;
    }

    public String getPriceProduct() {
        return mPriceProduct;
    }

    public void setPriceProduct(String mPriceProduct) {
        this.mPriceProduct = mPriceProduct;
    }

    public int getQuantityProduct() {
        return mQuantityProduct;
    }

    public void setQuantityProduct(int mNumberProduct) {
        this.mQuantityProduct = mNumberProduct;
    }

}
