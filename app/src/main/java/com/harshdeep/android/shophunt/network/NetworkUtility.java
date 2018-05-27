package com.harshdeep.android.shophunt.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtility {

    private static String FlipkartBaseAddress = "https://affiliate-api.flipkart.net/affiliate/1.0/search.json?query=";

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


            URL url=new URL(FlipkartBaseAddress+stringBuilder.toString()+"&resultCount=20");
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

        public static InputStream getAmazonResponse(String query){

            HttpURLConnection urlConnection;

//            String []array=query.split(" ");
//            StringBuilder stringBuilder = new StringBuilder();
//            for(int i=0;i<array.length-1;i++){
//                stringBuilder.append(array[i]);
//                stringBuilder.append("+");
//            }
//            stringBuilder.append(array[array.length-1]);

            SignedRequestsHelper helper;

            try {
                helper = new SignedRequestsHelper();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            String requestUrl = null;

            Map<String, String> params = new HashMap<String, String>();

            params.put("Service", "AWSECommerceService");
            params.put("Operation", "ItemSearch");
//            params.put("AWSAccessKeyId", "AKIAJIPQKZ2LJUWMMCSA");
            params.put("AssociateTag", "hssahdev-21");
            params.put("SearchIndex", "All");
            params.put("Keywords", query);
            params.put("ResponseGroup", "Images,ItemAttributes,ItemIds,Offers");

            requestUrl = helper.sign(params);
            Log.v("aurl",requestUrl);

            try {
                URL url = new URL(requestUrl);
                urlConnection= (HttpURLConnection) url.openConnection();
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
//                    inputStream.close();

                    Log.v("aws",(builder.toString()));
                    return inputStream;
                }else
                    Log.v("aws","wrong request");


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }