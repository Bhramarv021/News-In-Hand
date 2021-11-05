package com.example.bhramar.newsinhand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> News) {
        super(context, 0, News);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_items, parent, false);

        }
        News currentNews = getItem(position);
        TextView sectionView = listItemView.findViewById(R.id.sectionNameText);
        assert currentNews != null;
        sectionView.setText(currentNews.getSectionName());
        TextView webTitleView = listItemView.findViewById(R.id.webTitleText);
        webTitleView.setText(currentNews.getWebTitle());
        TextView authorView = listItemView.findViewById(R.id.authorText);
        StringBuilder author = new StringBuilder();
        for (int i = 0; i < currentNews.getAuthor().size(); i++)
            author.append(currentNews.getAuthor().get(i));
        authorView.setText(author);
        TextView publishDateView = listItemView.findViewById(R.id.pubDateText);
        try {
            Date pubDate = formatString(currentNews.getPublicationDate());
            publishDateView.setText(formatDate(pubDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private Date formatString(String data) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = format.parse(data);
        return date;

    }
}