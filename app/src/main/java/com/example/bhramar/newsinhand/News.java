package com.example.bhramar.newsinhand;

import java.util.ArrayList;

/**
 * News class represents an news event.
 */
public class News {

    private String mSectionName;
    private String mWebTitle;
    private ArrayList<String> mAuthor;
    private String mPublicationDate;
    private String mUrl;

    public News(String mSectionName, String mWebTitle, ArrayList<String> mAuthor, String mPublicationDate, String mUrl) {
        this.mSectionName = mSectionName;
        this.mWebTitle = mWebTitle;
        this.mAuthor = mAuthor;
        this.mPublicationDate = mPublicationDate;
        this.mUrl = mUrl;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public ArrayList<String> getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

}