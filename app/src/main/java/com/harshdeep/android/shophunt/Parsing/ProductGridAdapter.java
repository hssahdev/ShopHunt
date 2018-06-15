package com.harshdeep.android.shophunt.Parsing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harshdeep.android.shophunt.Product;
import com.harshdeep.android.shophunt.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ViewHolder> {

    List<Product> products;


    @NonNull
    @Override
    public ProductGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View product;
        product = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_grid,parent,false);


        return new ProductGridAdapter.ViewHolder(product);
    }

    public ProductGridAdapter(List<Product> products) {
        this.products = products;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductGridAdapter.ViewHolder holder, int position) {

        Product current = products.get(position);

        ImageView imageView = holder.produvtView.findViewById(R.id.image);
        Picasso.get().load(current.getImageURL()).into(imageView);

        ImageView logoImage = holder.produvtView.findViewById(R.id.logo);

        if(current.isFlipkart())
            logoImage.setImageResource(R.drawable.flipkart_logo_detail);
        else
            logoImage.setImageResource(R.drawable.amazon_logo);

        TextView textView = (TextView)holder.produvtView.findViewById(R.id.productTitle);
        textView.setText(current.getProductTitle().trim());

        textView = holder.produvtView.findViewById(R.id.price);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("hi", "IN"));
        textView.setText(numberFormat.getCurrency().getSymbol()+numberFormat.format(current.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View produvtView;

        public ViewHolder(View view){
            super(view);
            produvtView=view;
        }
    }
}
