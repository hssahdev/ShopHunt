package com.harshdeep.android.shophunt;

public class FlipkartProduct extends Product {

    private int FlipkartPrice;
    private String FlipkartURL;
    
    public FlipkartProduct(String productTitle) {
        super(productTitle);
    }

    public int getFlipkartPrice() {
        return FlipkartPrice;
    }

    public void setFlipkartPrice(int flipkartPrice) {
        FlipkartPrice = flipkartPrice;
    }

    public void setFlipkartURL(String flipkartURL) {
        FlipkartURL = flipkartURL;
    }

    public String getFlipkartURL() {
        return FlipkartURL;
    }
}
