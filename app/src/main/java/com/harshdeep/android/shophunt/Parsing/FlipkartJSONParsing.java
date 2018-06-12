package com.harshdeep.android.shophunt.Parsing;

import com.harshdeep.android.shophunt.FlipkartProduct;
import com.harshdeep.android.shophunt.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlipkartJSONParsing {

    private String JSON;

    public FlipkartJSONParsing(String json){
        JSON=json;
    }

    public List<Product> getProducts(){

        List<Product> products = null;

        try {
            if(JSON==null)
                return null;

            JSONObject root = new JSONObject(JSON);
            JSONArray productsArray = root.getJSONArray("products");

            products = new ArrayList<>();


            for(int i=0;i<productsArray.length();i++){

                String title = productsArray.getJSONObject(i).getJSONObject("productBaseInfoV1").getString("title");
                String imageURL = productsArray.getJSONObject(i).getJSONObject("productBaseInfoV1").getJSONObject("imageUrls").getString("800x800");
                int price = (int) productsArray.getJSONObject(i).getJSONObject("productBaseInfoV1").getJSONObject("flipkartSpecialPrice").getDouble("amount");
                String productURL = productsArray.getJSONObject(i).getJSONObject("productBaseInfoV1").getString("productUrl");


                FlipkartProduct product = new FlipkartProduct(title);
                product.setFlipkartPrice(price);
                product.setImageURL(imageURL);
                product.setFlipkartURL(productURL);

                products.add(product);
            }

            if (products.size()==0)
                return null;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return products;
    }

}
