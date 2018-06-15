package com.harshdeep.android.shophunt;

import android.support.annotation.NonNull;

public class Product implements Comparable<Product> {
    private String productTitle;

    private int price;
    private String ImageURL;

    public boolean isFlipkart;

    public Product() {
    }

    public Product(String productTitle) {
        this.productTitle = productTitle;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {

        this.price = price;
    }

    public boolean isFlipkart() {
        return isFlipkart;
    }

    public void setFlipkart(boolean flipkart) {
        isFlipkart = flipkart;
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

    @Override
    public int compareTo(@NonNull Product product) {
        if(price>product.price)
            return 1;
        else if(price<product.price)
            return -1;
        else
            return 0;
    }
}
