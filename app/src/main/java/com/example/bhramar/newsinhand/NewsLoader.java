package com.example.bhramar.newsinhand;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;
    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        Log.e(LOG_TAG, "In loadInBackground() method");
        if (mUrl == null) {
            return null;
        }
        //Perform the network request, parse the response and extract data.
        return QueryHandler.fetchNewsData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
