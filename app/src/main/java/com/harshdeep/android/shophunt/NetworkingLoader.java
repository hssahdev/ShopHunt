package com.harshdeep.android.shophunt;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public List<Product> loadInBackground() {
        List <Product>AmazonProducts,FlipkartProducts, finallist;
        finallist=new ArrayList<>();
        FlipkartJSONParsing flipkartJSONParsing = new FlipkartJSONParsing(NetworkUtility.createURLandGetJSONResponseFlipkart(keyword));
        AmazonXMLParsing amazonXMLParsing = new AmazonXMLParsing(NetworkUtility.getAmazonResponse(keyword));
        try {
            AmazonProducts = amazonXMLParsing.getProducts();
            FlipkartProducts = flipkartJSONParsing.getProducts();

            if(AmazonProducts!=null)
            AmazonProducts.remove(AmazonProducts.size()-1);

            int fp=0,am=0;

            if(FlipkartProducts == null && AmazonProducts== null)
                return null;
            else if(FlipkartProducts == null && AmazonProducts!= null){
                finallist=AmazonProducts;
            }else if(FlipkartProducts != null && AmazonProducts == null){
                finallist=FlipkartProducts;
            }
            else {

                while (true) {
                    if (fp < FlipkartProducts.size()) {
                        finallist.add(FlipkartProducts.get(fp));
                        fp++;
                    }

                    if (am < AmazonProducts.size()) {
                        finallist.add(AmazonProducts.get(am));
                        am++;
                    }

                    if (am >= AmazonProducts.size() && fp >= FlipkartProducts.size())
                        break;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finallist;
    }
}
