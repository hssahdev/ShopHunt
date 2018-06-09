package com.harshdeep.android.shophunt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.harshdeep.android.shophunt.Parsing.ProductListAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks {

    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        editText.clearFocus();

        if(getSupportLoaderManager().getLoader(0)!=null)
            getSupportLoaderManager().initLoader(0,null,this);



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

    private void processRequest(EditText editText, View view1){

        keyword=editText.getText().toString().trim();

        hideKeyboard(editText);

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
        View gridview,listview;
        gridview=findViewById(R.id.gridView);
        listview=findViewById(R.id.listView);
        if (id == R.id.view_toggle) {
            if(!preferences.getBoolean("isList",true))
            {
                item.setIcon(R.drawable.round_view_module_white_36dp);
                gridview.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                Toast.makeText(this, "View Changed to List", Toast.LENGTH_SHORT).show();
                editor.putBoolean("isList",true);
            }
            else{
                item.setIcon(R.drawable.round_view_list_white_36dp);
                Toast.makeText(this, "View Changed to Grid", Toast.LENGTH_SHORT).show();
                listview.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
                editor.putBoolean("isList",false);
            }
            editor.apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        ListView listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyView));

        GridView gridView = findViewById(R.id.gridView);
        gridView.setEmptyView(findViewById(R.id.emptyView));


        View view = findViewById(R.id.emptyView);
        ImageView imageView = view.findViewById(R.id.nulllist);
        imageView.setImageResource(R.drawable.search_error);
        final List list = (List) data;

        if(list!=null){
            ProductListAdapter listAdapter = new ProductListAdapter(this,0,list);
            listView.setAdapter(listAdapter);
            gridView.setAdapter(listAdapter);
        }else{
            view.setVisibility(View.VISIBLE);
        }

        if(isListView()) {

            gridView.setVisibility(View.GONE);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Product current = (Product) list.get(i);
                    Intent web = new Intent();
                    web.setAction(Intent.ACTION_VIEW);


                    if (current.isFlipkart) {
                        FlipkartProduct bss = (FlipkartProduct) current;
                        web.setData(Uri.parse(bss.getFlipkartURL()));
                    } else {
                        AmazonProduct am = (AmazonProduct) current;
                        web.setData(Uri.parse(am.getAmazonURL()));
                    }
                    startActivity(web);
                }
            });

        }
        else{
            listView.setVisibility(View.GONE);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Product current = (Product) list.get(i);
                    Intent web = new Intent();
                    web.setAction(Intent.ACTION_VIEW);


                    if (current.isFlipkart) {
                        FlipkartProduct bss = (FlipkartProduct) current;
                        web.setData(Uri.parse(bss.getFlipkartURL()));
                    } else {
                        AmazonProduct am = (AmazonProduct) current;
                        web.setData(Uri.parse(am.getAmazonURL()));
                    }
                    startActivity(web);
                }
            });
        }
        loader.abandon();

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }
}
