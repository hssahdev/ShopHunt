package com.harshdeep.android.shophunt;

public class FlipkartProduct extends Product {

    private String FlipkartURL;
    
    public FlipkartProduct(String productTitle) {
        super(productTitle);
        setFlipkart(true);
    }

    public int getFlipkartPrice() {
        return getPrice();
    }

    public void setFlipkartPrice(int flipkartPrice) {
        setPrice(flipkartPrice);
    }

    public void setFlipkartURL(String flipkartURL) {
        FlipkartURL = flipkartURL;
    }

    public String getFlipkartURL() {
        return FlipkartURL;
    }
}
