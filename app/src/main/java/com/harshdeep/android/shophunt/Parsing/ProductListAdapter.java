package com.harshdeep.android.shophunt.Parsing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.harshdeep.android.shophunt.FlipkartProduct;
import com.harshdeep.android.shophunt.Product;
import com.harshdeep.android.shophunt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends ArrayAdapter<Product> {

    public ProductListAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_item,parent,false);
        }

        FlipkartProduct current = (FlipkartProduct) getItem(position);

        ImageView imageView = convertView.findViewById(R.id.image);
        Picasso.get().load(current.getImageURL()).into(imageView);

        TextView textView = (TextView)convertView.findViewById(R.id.productTitle);
        textView.setText(current.getProductTitle().trim());

        textView = convertView.findViewById(R.id.Flipkartprice);
        textView.setText("₹ "+current.getFlipkartPrice());


        return convertView;
    }
}
