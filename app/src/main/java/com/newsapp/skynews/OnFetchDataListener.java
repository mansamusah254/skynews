package com.newsapp.skynews;

import com.newsapp.skynews.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onError(String message);
}
