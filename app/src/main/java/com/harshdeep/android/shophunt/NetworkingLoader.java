package com.harshdeep.android.shophunt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.harshdeep.android.shophunt.Parsing.AmazonXMLParsing;
import com.harshdeep.android.shophunt.Parsing.FlipkartJSONParsing;
import com.harshdeep.android.shophunt.network.NetworkUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetworkingLoader extends AsyncTaskLoader<List<Product>> {

    String keyword;

    public NetworkingLoader(@NonNull Context context, String keyword) {
        super(context);
        this.keyword=keyword;
    }

    @Nullable
    @Override
    public List<Product> loadInBackground() {
        List <Product>AmazonProducts,FlipkartProducts, finallist;
        finallist=new ArrayList<>();
        FlipkartJSONParsing flipkartJSONParsing = new FlipkartJSONParsing(NetworkUtility.createURLandGetJSONResponseFlipkart(keyword));
        AmazonXMLParsing amazonXMLParsing = new AmazonXMLParsing(NetworkUtility.getAmazonResponse(keyword));
        try {
            AmazonProducts = amazonXMLParsing.getProducts();
            if(AmazonProducts==null)
                return null;
            FlipkartProducts = flipkartJSONParsing.getProducts();
            for (int i=0;i<FlipkartProducts.size();i++){
                AmazonProduct yo = (AmazonProduct) AmazonProducts.get(i);
                String title = yo.getProductTitle();
                String URL = yo.getImageURL();
                String prURL = yo.getAmazonURL();
                int price = yo.getPrice();

                System.out.println(title+" "+URL+" "+prURL+" "+price);
                finallist.add(yo);
                finallist.add(FlipkartProducts.get(i));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finallist;
    }
}
