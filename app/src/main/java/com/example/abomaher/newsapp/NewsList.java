package com.example.abomaher.newsapp;

import android.net.Uri;

import java.net.URL;

public class NewsList {
    String sectionID;
    String Title;
    String Des;
    String PublishedAt;
    String url;
    String author;

    public NewsList(String sectionID, String title, String des, String author, String published, String url1) {
        this.sectionID = sectionID;
        this.Des = des;
        this.author = author;
        this.PublishedAt = published;
        this.Title = title;
        this.url = url1;

    }

    public String getSectionID() {
        return sectionID;
    }

    public String getDes() {
        return Des;
    }

    public String getTitle() {
        return Title;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }
}
