package com.example.bhramar.newsinhand;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryHandler {
    private static final String LOG_TAG = QueryHandler.class.getSimpleName();

    public static List<News> fetchNewsData(String requestUrl) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing InputStream", e);
        }
        return extractDataFromJson(jsonResponse);
    }

    //Creating URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating Url.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response Code : " + urlConnection.getResponseCode());

            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in retrieving data ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractDataFromJson(String newsJson) {

        //If newsJson is null and empty ,return null.
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        List<News> newsList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(newsJson);

            JSONObject response = root.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String sectionName = currentNews.getString("sectionName");
                String webTitle = currentNews.getString("webTitle");
                String webUrl = currentNews.getString("webUrl");
                String pubDate = currentNews.getString("webPublicationDate");
                JSONArray tags = currentNews.getJSONArray("tags");
                ArrayList<String> author = new ArrayList<>();
                for (int j = 0; j < tags.length(); j++) {
                    JSONObject currentTag = tags.getJSONObject(j);
                    author.add(currentTag.getString("webTitle"));
                }
                newsList.add(new News(sectionName, webTitle, author, pubDate, webUrl));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem in parsing JSON response results ", e);
        }
        return newsList;
    }
}
