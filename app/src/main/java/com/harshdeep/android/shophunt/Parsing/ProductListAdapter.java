package com.harshdeep.android.shophunt.Parsing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harshdeep.android.shophunt.AmazonProduct;
import com.harshdeep.android.shophunt.FlipkartProduct;
import com.harshdeep.android.shophunt.Product;
import com.harshdeep.android.shophunt.R;
import com.harshdeep.android.shophunt.WebView_Activity;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    List<Product> products;
    Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View product;
        product = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_list,parent,false);


        return new ViewHolder(product);
    }

    public ProductListAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Product current = products.get(position);

        ImageView imageView = holder.produvtView.findViewById(R.id.image);
        Picasso.get().load(current.getImageURL()).into(imageView);

        ImageView logoImage = holder.produvtView.findViewById(R.id.logo);

        if(current.isFlipkart())
            logoImage.setImageResource(R.drawable.ic_flipkart_icon);
        else
            logoImage.setImageResource(R.drawable.ic_icons8_amazon_2);

        TextView textView = (TextView)holder.produvtView.findViewById(R.id.productTitle);
        textView.setText(current.getProductTitle().trim());

        int MRP = current.getMRP();
        int price = current.getPrice();

        textView = holder.produvtView.findViewById(R.id.price);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("hi", "IN"));
        textView.setText(numberFormat.getCurrency().getSymbol()+numberFormat.format(price));


        if(price!=MRP && MRP>price){
            textView = holder.produvtView.findViewById(R.id.MRP);
            textView.setText(numberFormat.getCurrency().getSymbol()+numberFormat.format(MRP));
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            textView = holder.produvtView.findViewById(R.id.discount);
            int discount = ((MRP - price)*100)/ MRP;
            textView.setText(discount+"% OFF");
        }else {
            ((TextView)holder.produvtView.findViewById(R.id.MRP)).setText("");
            ((TextView)holder.produvtView.findViewById(R.id.discount)).setText("");
        }


        holder.produvtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product product = products.get(position);
                Intent web = new Intent(context, WebView_Activity.class);

                if (product.isFlipkart) {
                    FlipkartProduct bss = (FlipkartProduct) product;
                    web.putExtra("url",bss.getFlipkartURL());
                } else {
                    AmazonProduct am = (AmazonProduct) product;
                    web.putExtra("url",am.getAmazonURL());

                }
                context.startActivity(web);
            }
        });
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
