package com.harshdeep.android.shophunt;

public class Product {
    private String productTitle;


    private String ImageURL;

    public Product() {
    }

    public Product(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;

    }
}
