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
import java.util.Comparator;
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

            int fp=0,am=0;

            if(FlipkartProducts == null || AmazonProducts== null)
                return null;

            while (true){
                if(fp<FlipkartProducts.size()){
                    finallist.add(FlipkartProducts.get(fp));
                    fp++;
                }

                if(am<AmazonProducts.size()-1){
                    finallist.add(AmazonProducts.get(am));
                    am++;
                }

                if(am>=AmazonProducts.size()-1 && fp>=FlipkartProducts.size())
                    break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Comparator<? super Product> comparatorAsc = new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                if(product.getPrice()>t1.getPrice())
                    return 1;
                else if(product.getPrice()<t1.getPrice())
                    return -1;
                else
                    return 0;
            }
        };

        Comparator<? super Product> comparatorDesc = new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                if(product.getPrice()>t1.getPrice())
                    return -1;
                else if(product.getPrice()<t1.getPrice())
                    return 1;
                else
                    return 0;
            }
        };

        switch (FilterDialogBox.finaly){
            case 0:
                finallist.sort(comparatorAsc);
                break;

            case 1:
                finallist.sort(comparatorDesc);
                break;
        }

        return finallist;
    }
}
