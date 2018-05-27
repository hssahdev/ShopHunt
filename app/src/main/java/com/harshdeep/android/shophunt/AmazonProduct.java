package com.harshdeep.android.shophunt;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class AmazonProduct extends Product {

    private int AmazonPrice;

    private String AmazonURL;

    public AmazonProduct() {
        super();
    }

    public int getAmazonPrice() {
        return AmazonPrice;
    }

    public void setAmazonPrice(int amazonPrice) {
        AmazonPrice = amazonPrice;
    }

    public String getAmazonURL() {
        return AmazonURL;
    }

    public void setAmazonURL(String amazonURL) {
        AmazonURL = amazonURL;
    }

    public AmazonProduct readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (parser.next()!=XmlPullParser.END_TAG){
            if (parser.getEventType()!=XmlPullParser.START_TAG)
                continue;

            if(parser.getName().equals("Item"))
            {
                while (parser.next()!=XmlPullParser.END_TAG){
                    if (parser.getEventType()!=XmlPullParser.START_TAG)
                        continue;
                    if(parser.getName().equals("MoreSearchResultsUrl")){
                        Log.v("awsResposeParsing",parser.getText());

                    }
                }
            }
        }
        return null;
    }

    private String readAmazonURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "DetailPageURL");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "DetailPageURL");
        Log.v("amazonproduct",title);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
