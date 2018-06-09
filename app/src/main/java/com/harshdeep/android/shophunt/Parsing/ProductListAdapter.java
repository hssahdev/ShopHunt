package com.harshdeep.android.shophunt.Parsing;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.harshdeep.android.shophunt.Product;
import com.harshdeep.android.shophunt.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends ArrayAdapter<Product> {

    public ProductListAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            if(isListView())
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item_list,parent,false);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item_grid,parent,false);
        }

        Product current = getItem(position);

        ImageView imageView = convertView.findViewById(R.id.image);
        Picasso.get().load(current.getImageURL()).into(imageView);

        ImageView logoImage = convertView.findViewById(R.id.logo);

        if(current.isFlipkart())
            logoImage.setImageResource(R.drawable.flipkart_logo_detail);
        else
            logoImage.setImageResource(R.drawable.amazon_logo);

        TextView textView = (TextView)convertView.findViewById(R.id.productTitle);
        textView.setText(current.getProductTitle().trim());

        textView = convertView.findViewById(R.id.price);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("hi", "IN"));
        textView.setText(numberFormat.getCurrency().getSymbol()+numberFormat.format(current.getPrice()));


        return convertView;
    }

    private boolean isListView(){
        SharedPreferences preferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        return preferences.getBoolean("isList",true);

    }
}
