package com.example.bhramar.newsinhand;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private String LOG_TAG = NewsActivity.class.getSimpleName();

    //Used uri.Builder in onCreateLoader() method
//    private String url = "https://content.guardianapis.com/search?show-tags=contributor&api-key=test";

    private TextView mEmptyTextView;

    NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);

        ListView list_view = findViewById(R.id.list);
        mEmptyTextView = findViewById(R.id.emptyTextView);
        list_view.setEmptyView(mEmptyTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        list_view.setAdapter(mAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                assert currentNews != null;
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager.getInstance(this).initLoader(1, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loadingIndicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String url = "https://content.guardianapis.com/search?show-tags=contributor&api-key=test";
        // If query add here
        String query = "";
        // Add query value here
        String value = "";
        Uri.Builder baseUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(query, value);
        return new NewsLoader(this, baseUri.toString());
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loadingIndicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyTextView.setText(R.string.no_news);

        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }
}