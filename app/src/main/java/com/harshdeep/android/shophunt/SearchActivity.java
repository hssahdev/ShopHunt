package com.harshdeep.android.shophunt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.harshdeep.android.shophunt.Parsing.ProductGridAdapter;
import com.harshdeep.android.shophunt.Parsing.ProductListAdapter;
import com.harshdeep.android.shophunt.network.NetworkingLoader;

import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks , FilterDialogBox.GetList{

    String keyword;
    List<Product> productList;
    private AdView mAdView;
    private RecyclerView recyclerView;
    ProductListAdapter listAdapter;
    ProductGridAdapter gridAdapter;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        MobileAds.initialize(this, "ca-app-pub-2631882660749155~1074356874");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.recyclerView);

        AppRater.app_launched(this);

        Log.v("token", FirebaseInstanceId.getInstance().getToken());

         fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setVisibility(View.INVISIBLE);
        final FilterDialogBox dialogBox = new FilterDialogBox();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBox.show(getSupportFragmentManager(),null);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final EditText editText=findViewById(R.id.Key);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
            findViewById(R.id.startView).setVisibility(View.GONE);

        }



        final View view1 = findViewById(R.id.emptyView);
        view1.setVisibility(View.GONE);

        editText.setImeOptions(EditorInfo.IME_ACTION_GO);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_DONE
                        ) {
                    processRequest(editText,view1);
                    return true;

                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });


        findViewById(R.id.arrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processRequest(editText,view1);
            }
        });
    }

    public void processRequest(EditText editText, View view1){

        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.startView).setVisibility(View.GONE);

        keyword=editText.getText().toString().trim();

        hideKeyboard(editText);
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

    private void hideKeyboard(View editText){
        InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to quit?");
            builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SearchActivity.super.onBackPressed();
                }
            });
            builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.create().show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);

        if(preferences.getBoolean("isList",true))
        {
            menu.findItem(R.id.view_toggle).setIcon(R.drawable.round_view_module_white_36dp);
        }
        else{
            menu.findItem(R.id.view_toggle).setIcon(R.drawable.round_view_list_white_36dp);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.drawer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //noinspection SimplifiableIfStatement

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        if (id == R.id.view_toggle) {
            if(!preferences.getBoolean("isList",true))
            {
                item.setIcon(R.drawable.round_view_module_white_36dp);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(listAdapter);
                Toast.makeText(this, "View Changed to List", Toast.LENGTH_SHORT).show();
                editor.putBoolean("isList",true);
            }
            else{
                item.setIcon(R.drawable.round_view_list_white_36dp);
                Toast.makeText(this, "View Changed to Grid", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(gridAdapter);
                editor.putBoolean("isList",false);
            }
            editor.apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortListAndNotifyAdapter(){

        Comparator<? super Product> comparatorAsc = new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                if(product.getPrice()>t1.getPrice())
                    return 1;
                else if(product.getPrice()<t1.getPrice())
                    return -1;
                else
                    return 0;
            }
        };

        Comparator<? super Product> comparatorDesc = new Comparator<Product>() {
            @Override
            public int compare(Product product, Product t1) {
                if(product.getPrice()>t1.getPrice())
                    return -1;
                else if(product.getPrice()<t1.getPrice())
                    return 1;
                else
                    return 0;
            }
        };

        switch (FilterDialogBox.finaly){
            case 0:
                productList.sort(comparatorAsc);
                break;

            case 1:

                productList.sort(comparatorDesc);
                break;
        }

        listAdapter.notifyDataSetChanged();
        gridAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_share) {
             Intent sendIntent = new Intent();
             sendIntent.setAction(Intent.ACTION_SEND);
             sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
             sendIntent.setType("text/url");
             startActivity(Intent.createChooser(sendIntent, "Share"));
        }else if(id == R.id.downloadFlipkart){
            Intent web = new Intent();
            web.setData(Uri.parse("http://affiliate.flipkart.com/install-app?affid=hssahdev252"));
            web.setAction(Intent.ACTION_VIEW);
            startActivity(web);
        }else if(id== R.id.downloadAmazon){
            Intent web = new Intent();
            web.setData(Uri.parse("https://play.google.com/store/apps/details?id=in.amazon.mShop.android.shopping&hl=en"));
            web.setAction(Intent.ACTION_VIEW);
            startActivity(web);

        }else if(id==R.id.rateOnPlaystore){
            Intent web = new Intent();
            web.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.harshdeep.android.shophunt"));
            web.setAction(Intent.ACTION_VIEW);
            startActivity(web);
        }else if(id == R.id.feedback){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("*/*");
            intent.setData(Uri.parse("mailto:"));
            String []emailId = {"hssahdev252@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, emailId);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new NetworkingLoader(this,keyword);
    }

    private boolean isListView(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        return preferences.getBoolean("isList",true);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        findViewById(R.id.progreeBar).setVisibility(View.GONE);


        View view = findViewById(R.id.emptyView);

        switch (NetworkingLoader.flag){
            case 1:
                Toast.makeText(this, "There seems an error getting FLipkart products, please try again!", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(this, "There seems an error getting Amazon products, please try again!", Toast.LENGTH_LONG).show();
                break;

        }


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.v("Ad","AdFailedtoLoad "+errorCode);
                mAdView.setVisibility(View.GONE);
            }

        });


        final List list = (List) data;

        fab.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        if(list!=null && list.size()!=0){
            recyclerView.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            productList=(List<Product>) data;
             listAdapter = new ProductListAdapter(productList,this);
             gridAdapter = new ProductGridAdapter(productList,this);

            if(isListView())
            {
                recyclerView.setAdapter(listAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
            else{
                recyclerView.setAdapter(gridAdapter);
                recyclerView.setLayoutManager(gridLayoutManager);
            }

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                View edit = findViewById(R.id.editlayout);

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
//                        edit.setVisibility(View.GONE);
                    } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                        fab.show();
//                        edit.setVisibility(View.VISIBLE);
                    }
                }
            });

        }else{
            Log.v("Null list","true");
            view.setVisibility(View.VISIBLE);
            ImageView imageView = view.findViewById(R.id.nulllist);
            imageView.setImageResource(R.drawable.search_error);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }

}
