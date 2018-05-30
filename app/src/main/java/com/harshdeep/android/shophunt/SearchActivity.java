package com.harshdeep.android.shophunt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
        findViewById(R.id.progreeBar).setVisibility(View.GONE);
        final List list = (List) data;
        ProductListAdapter listAdapter = new ProductListAdapter(this,0,list);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                FlipkartProduct current = (FlipkartProduct) list.get(i);
//                Intent web = new Intent(SearchActivity.this,WebsiteActivity.class);
//                web.putExtra("url",current.getFlipkartURL());
//                startActivity(web);
//            }
//        });
        loader.abandon();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }
}

