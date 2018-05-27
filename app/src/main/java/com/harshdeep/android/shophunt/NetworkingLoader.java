package com.harshdeep.android.shophunt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.harshdeep.android.shophunt.Parsing.AmazonXMLParsing;
import com.harshdeep.android.shophunt.Parsing.FlipkartJSONParsing;
import com.harshdeep.android.shophunt.network.NetworkUtility;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
        FlipkartJSONParsing flipkartJSONParsing = new FlipkartJSONParsing(NetworkUtility.createURLandGetJSONResponseFlipkart(keyword));
        AmazonXMLParsing amazonXMLParsing = new AmazonXMLParsing(NetworkUtility.getAmazonResponse(keyword));
        try {
            amazonXMLParsing.getProducts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return flipkartJSONParsing.getProducts();
    }
}
