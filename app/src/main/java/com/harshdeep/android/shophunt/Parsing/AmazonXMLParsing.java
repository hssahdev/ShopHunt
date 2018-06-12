package com.harshdeep.android.shophunt.Parsing;

import android.util.Log;

import com.harshdeep.android.shophunt.AmazonProduct;
import com.harshdeep.android.shophunt.Product;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AmazonXMLParsing {

    private static final String ns = null;

    InputStream input;

    public AmazonXMLParsing(InputStream input) {
        this.input = input;
    }

    public List<Product> getProducts() throws IOException {

        XmlPullParserFactory factory;
        ArrayList list = new ArrayList();
        if (input==null)
            return null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(input,null);
//            Log.v("tag", "blah");

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();

                switch (event) {

                    case XmlPullParser.END_TAG:
                        if(name.equals("Item"))
                            System.out.println("fount Item");
                        AmazonProduct product = new AmazonProduct();
                        product.parseXML(parser);
                        list.add(product);
//                        return null;
                        break;

                }
                event = parser.next();
            }
            }


        catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("pull",e.toString());
        } finally {
            input.close();
        }


        return list;
    }

}
