package com.harshdeep.android.shophunt;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtility {

    private static String FlipkartBaseAddress = "https://affiliate-api.flipkart.net/affiliate/1.0/search.json?query=";

    /*
     * Your Access Key ID, as taken from the Your Account page.
     */
    private static final String ACCESS_KEY_ID = "AKIAJIPQKZ2LJUWMMCSA";

    /*
     * Your Secret Key corresponding to the above ID, as taken from the
     * Your Account page.
     */
    private static final String SECRET_KEY = "qVRBsS6vxwvvefMFL+87J9BMT4RwOeb4lPeE338e";

    /*
     * Use the end-point according to the region you are interested in.
     */
    private static final String ENDPOINT = "webservices.amazon.in";



    private NetworkUtility(){

    }

    public static String createURLandGetJSONResponseFlipkart(String query) {

        String JSONResponse=null;
        HttpsURLConnection urlConnection=null;
        try {

            String []array=query.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<array.length-1;i++){
                stringBuilder.append(array[i]);
                stringBuilder.append("+");
            }
            stringBuilder.append(array[array.length-1]);


            URL url=new URL(FlipkartBaseAddress+stringBuilder.toString()+"&resultCount=5");
            Log.v("url",url.toString());

            urlConnection= (HttpsURLConnection) url.openConnection();
            urlConnection.addRequestProperty("Fk-Affiliate-Id","hssahdev252");
            urlConnection.addRequestProperty("Fk-Affiliate-Token","5701c05526e74de1a0f4356df6834a89");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){

                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader=new BufferedReader(reader);

                StringBuilder builder = new StringBuilder();
                String line=bufferedReader.readLine();

                while(line!=null){
                    builder.append(line);
                    line=bufferedReader.readLine();
                }
                inputStream.close();

                JSONResponse = builder.toString();
            }
    } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(urlConnection!=null)
            urlConnection.disconnect();
        }
//        Log.v("flip",JSONResponse);
            return JSONResponse;
        }

        public static String getAmazonResponse(String query){

            HttpURLConnection urlConnection;

            String []array=query.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for(int i=0;i<array.length-1;i++){
                stringBuilder.append(array[i]);
                stringBuilder.append("+");
            }
            stringBuilder.append(array[array.length-1]);

//        String rawURL = "http://webservices.amazon.in/onca/xml?Service=AWSECommerceService&Operation=ItemSearch&
//          SubscriptionId=AKIAJIPQKZ2LJUWMMCSA&AssociateTag=hssahdev-21&SearchIndex=All&Keywords="+stringBuilder.toString()+"&ResponseGroup=Images,ItemAttributes,ItemIds,Offers";

            String rawURL = "http://webservices.amazon.in/onca/xml?AWSAccessKeyId=AKIAJIPQKZ2LJUWMMCSA&AssociateTag=hssahdev-21&Keywords=iPhone&Operation=ItemSearch&ResponseGroup=Images%2CItemAttributes%2CItemIds%2COffers&SearchIndex=All&Service=AWSECommerceService&Timestamp=2018-04-12T16%3A21%3A27.000Z&Signature=j3swzsNiWwGz8k1UXs9NmPiVTg0hJT72h7ayzI3nttA%3D";
            try {
                URL url = new URL(rawURL);
                urlConnection= (HttpURLConnection) url.openConnection();
//                urlConnection.addRequestProperty("Fk-Affiliate-Id","hssahdev252");
//                urlConnection.addRequestProperty("Fk-Affiliate-Token","5701c05526e74de1a0f4356df6834a89");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);

                urlConnection.connect();

                if(urlConnection.getResponseCode()==200){

                    InputStream inputStream=urlConnection.getInputStream();
                    InputStreamReader reader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader bufferedReader=new BufferedReader(reader);

                    StringBuilder builder = new StringBuilder();
                    String line=bufferedReader.readLine();

                    while(line!=null){
                        builder.append(line);
                        line=bufferedReader.readLine();
                    }
                    inputStream.close();

                    Log.v("aws",(builder.toString()));
                    return builder.toString();
                }else
                    Log.v("aws","wrong request");


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
