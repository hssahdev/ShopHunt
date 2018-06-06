package com.harshdeep.android.shophunt;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.harshdeep.android.shophunt.Parsing.ProductListAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText editText=findViewById(R.id.Key);
        editText.clearFocus();

        if(getSupportLoaderManager().getLoader(0)!=null)
            getSupportLoaderManager().initLoader(0,null,this);



        final View view1 = findViewById(R.id.emptyView);
        view1.setVisibility(View.GONE);


        findViewById(R.id.arrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                keyword=editText.getText().toString().trim();
                hideKeyboard(SearchActivity.this);
                editText.clearFocus();

                if(!isConnectedtoInternet()){
                    view1.setVisibility(View.VISIBLE);
                    ImageView imageView = view1.findViewById(R.id.nulllist);
                    imageView.setImageResource(R.drawable.nointernet_r_2x);

                }
                else if(keyword.length()==0){
                    editText.setError("This cannot be empty");
                }else {
                    editText.setError(null);
                    findViewById(R.id.progreeBar).setVisibility(View.VISIBLE);
                    getSupportLoaderManager().restartLoader(0, null, SearchActivity.this).forceLoad();
                }
            }
        });

    }

    private void hideKeyboard(AppCompatActivity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean isConnectedtoInternet(){

        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        boolean isConnected = networkInfo!=null && networkInfo.isConnectedOrConnecting();
        Log.v("IsConnected",isConnected+"");
        return isConnected;
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new NetworkingLoader(this,keyword);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        findViewById(R.id.progreeBar).setVisibility(View.GONE);
        ListView listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyView));

        View view = findViewById(R.id.emptyView);
        ImageView imageView = view.findViewById(R.id.nulllist);
        imageView.setImageResource(R.drawable.search_error);
        final List list = (List) data;
        if(list!=null){
        ProductListAdapter listAdapter = new ProductListAdapter(this,0,list);
        listView.setAdapter(listAdapter);
        }else{
            view.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product current = (Product) list.get(i);
                Intent web = new Intent(SearchActivity.this,WebsiteActivity.class);
                String url;
                if(current.isFlipkart){
                    FlipkartProduct bss = (FlipkartProduct)current;
                    web.putExtra("url",bss.getFlipkartURL());
                }
                else {
                    AmazonProduct am = (AmazonProduct)current;
                    web.putExtra("url",am.getAmazonURL());
                }
                startActivity(web);
            }
        });
        loader.abandon();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }
}

