package com.harshdeep.android.shophunt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText editText=findViewById(R.id.Key);

        if(getSupportLoaderManager().getLoader(0)!=null)
            getSupportLoaderManager().initLoader(0,null,this);

        findViewById(R.id.arrowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword=editText.getText().toString().trim();

                findViewById(R.id.progreeBar).setVisibility(View.VISIBLE);
                getSupportLoaderManager().restartLoader(0,null,SearchActivity.this).forceLoad();
            }
        });



    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        return new NetworkingLoader(this,keyword);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        String obj = (String) data;
        TextView t = findViewById(R.id.text);
        findViewById(R.id.progreeBar).setVisibility(View.GONE);
        if(obj!=null)
        t.setText(obj);
        else
            t.setText("not");
        loader.abandon();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }
}

class NetworkingLoader extends AsyncTaskLoader<String>{

    String keyword;

    public NetworkingLoader(@NonNull Context context, String keyword) {
        super(context);
        this.keyword=keyword;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtility.getAmazonResponse(keyword);
    }
}

