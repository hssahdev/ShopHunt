package com.harshdeep.android.shophunt;

public class Product {
    private String productTitle;

    private int price;
    private String ImageURL;

    boolean isFlipkart;

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
}
