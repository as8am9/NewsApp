package com.example.abomaher.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class Adapter extends ArrayAdapter {
    public Adapter(@NonNull Context context, ArrayList<NewsList> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }
        NewsList list = (NewsList) getItem(position);
        TextView txtSectionID = (TextView) view.findViewById(R.id.list_item_sectionID);
        txtSectionID.setText(list.getSectionID());
        TextView txtAuthorName = (TextView)view.findViewById(R.id.list_item_authorName);
        txtAuthorName.setText(list.getAuthor());
        TextView txtPublished = (TextView) view.findViewById(R.id.list_item_publishedAt);
        txtPublished.setText(list.getPublishedAt().replace("T", " "));
        TextView txtTitle = (TextView) view.findViewById(R.id.list_item_title);
        txtTitle.setText(list.getTitle());
        TextView txtDes = (TextView) view.findViewById(R.id.list_item_description);
        txtDes.setText(list.getDes());

        return view;
    }
}
