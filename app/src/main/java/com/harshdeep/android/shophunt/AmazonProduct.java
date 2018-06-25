package com.harshdeep.android.shophunt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class AmazonProduct extends Product {


    private String AmazonURL;

    public AmazonProduct() {
        super();
        setFlipkart(false);
    }

    public String getAmazonURL() {
        return AmazonURL;
    }

    public void setAmazonURL(String amazonURL) {
        AmazonURL = amazonURL;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public void parseXML(XmlPullParser parser) {
        int event;
        boolean [] flag=new boolean[5];

        try {
//            parser.require(XmlPullParser.START_TAG, null, "Item");
            event = parser.getEventType();


        while (event!=XmlPullParser.END_DOCUMENT) {

            switch (event) {
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("DetailPageURL")){
//                        System.out.println(readText(parser));
                        setAmazonURL(readText(parser));
                        flag[0]=true;
                    }
                    else if(parser.getName().equals("LargeImage") && !flag[1]){
                        parser.nextTag();
//                        System.out.println(readText(parser));
                        setImageURL(readText(parser));
                        flag[1]=true;
                    }
                    else if(parser.getName().equals("ListPrice")){
                        while(!parser.getName().equals("Amount"))
                            parser.nextTag();
                        int mrp=Integer.parseInt(readText(parser))/100;
//                        Log.v("MRP aws",""+mrp);
                        setMRP(mrp);
                        flag[2]=true;
                    }
                    else if (parser.getName().equals("Title")) {
//                        System.out.println(readText(parser));
                        setProductTitle(readText(parser));
                        flag[3]=true;
                    }
                    else if(parser.getName().equals("Price")){
                        while(!parser.getName().equals("Amount"))
                            parser.nextTag();
//                        System.out.println(readText(parser));
                        setPrice(Integer.parseInt(readText(parser))/100);
                        flag[4]=true;
                    }
            }
            if(flag[0] && flag[1] && flag[2] && flag[3] && flag[4])
                return;
            event = parser.next();
        }
        }catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
