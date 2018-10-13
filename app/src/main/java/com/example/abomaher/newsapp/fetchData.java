package com.example.abomaher.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
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
import java.util.ArrayList;
import java.util.List;

public class fetchData extends AsyncTaskLoader<ArrayList<NewsList>> {
    StringBuilder output;
    String LOG_TAG = MainActivity.class.getSimpleName();
    int connectTimeOut = 15000;
    int readTimeOut = 10000;
    int responseCode = 200;
    private String mUrl;
    private String apiUrl ="http://content.guardianapis.com/search?q=debates&show-tags=contributor&api-key=e34f1803-ccb2-4702-a67f-a6df9d8d12e3";

    public fetchData(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<NewsList> loadInBackground() {
        ArrayList<NewsList> arrayList = new ArrayList<>();
        try {
            output = new StringBuilder();
            URL url = new URL(mUrl);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            //LOG
            Log.i(LOG_TAG, "HttpUrlOpenConnection");
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setConnectTimeout(connectTimeOut);
            httpUrlConnection.setReadTimeout(readTimeOut);
            if (httpUrlConnection.getResponseCode() == responseCode) {
                //LOG
                Log.i(LOG_TAG, "HttpUrlConnection Responce Code == 200");
                InputStream inputStream = httpUrlConnection.getInputStream();
                if (inputStream != null) {
                    //LOG
                    Log.i(LOG_TAG, "InputStream !=null");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String Line = "";
                    Line = bufferedReader.readLine();
                    while (Line != null) {
                        output.append(Line);
                        Line = bufferedReader.readLine();
                    }
                    if (output != null) {
                        JSONObject jsonObject = new JSONObject(output.toString());
                        JSONObject response = jsonObject.getJSONObject("response");
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);
                            String section = obj.getString("sectionName");
                            String title = obj.getString("webTitle");
                            String date = obj.getString("webPublicationDate");
                            String url1 = obj.getString("webUrl");
                            String description = "";
                            String author = "";
                                JSONArray tags = obj.getJSONArray("tags");
                                if (tags.length()!=0) {
                                    JSONObject object = tags.getJSONObject(0);
                                    if (object.has("bio")) {
                                        description = object.optString("bio");
                                    }
                                    if (object.has("webTitle")) {
                                        author = object.optString("webTitle");
                                    }
                                }
                            arrayList.add(new NewsList(section, title, description, author, date, url1));
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}