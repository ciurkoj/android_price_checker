package com.example.pricechecker.fragments;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class ShoppingResult {
    private int position;
    private String title;
    private String link;
    private String source;
    private String price;
    private Float extracted_price;
    private String snippet;
    private String thumbnail;

    public ShoppingResult(int position, String title,
                          String link,
                          String source,
                          String price,
                          Float extracted_price,
                          String snippet,
                          String thumbnail) {
        this.position = position;
        this.title = title;
        this.link = link;
        this.source = source;
        this.price = price;
        this.extracted_price = extracted_price;
        this.snippet = snippet;
        this.thumbnail = thumbnail;


    }

    public String getResultTitle() {
        return title;
    }

    public String getResultSource() {
        return source;
    }

    public String getResultPrice() {
        return price;
    }

    public String getResultThumbnail() {
        return thumbnail;
    }


}
