package com.example.abomaher.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsList>> {
    ArrayList<NewsList> arrayList = new ArrayList<NewsList>();
    ListView lv;
    Adapter adapter;
    View loadingIndicator;
    ImageButton refresh, set;
    private TextView mEmptyStateTextView;
    private String apiUrl = "https://content.guardianapis.com/search?";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        refresh = (ImageButton) findViewById(R.id.refresh);
        set = (ImageButton) findViewById(R.id.set);
        loadingIndicator = findViewById(R.id.loading_indicator);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    adapter.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                loadingIndicator.setVisibility(View.INVISIBLE);
                if (networkInfo != null && networkInfo.isConnected()) {
                    getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, MainActivity.this).forceLoad();
                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setText(R.string.no_internet);
                }
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                                       startActivity(intent);
                                   }
                               }
        );
        lv = (ListView) findViewById(R.id.activity_main_list);
        lv.setEmptyView(mEmptyStateTextView);
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        } else {
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(arrayList.get(i).getUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<ArrayList<NewsList>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(apiUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "e34f1803-ccb2-4702-a67f-a6df9d8d12e3");
        uriBuilder.appendQueryParameter("q", topic);
        uriBuilder.appendQueryParameter("from-date", "2016-01-01");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("show-fields", "trailText");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        return new fetchData(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsList>> loader, ArrayList<NewsList> newsLists) {
        loadingIndicator.setVisibility(View.GONE);
        arrayList = newsLists;
        if (arrayList != null) {
            adapter = new Adapter(getApplicationContext(), arrayList);
            lv.setAdapter(adapter);
        }
        mEmptyStateTextView.setText(R.string.no_news);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsList>> loader) {
        adapter.clear();
    }
}
