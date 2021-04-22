package com.example.pricechecker.fragments;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Item extends ArrayList<String> {
    private String itemTitle;
    private String itemPrice;
    private String itemSource;
    private String thumbnailUrl;

    public Item(String itemTitle, String itemPrice, String itemSource, String thumbnailUrl) {
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
        this.itemSource = itemSource;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getItemTitle() {return this.itemTitle;}
    public String getItemPrice() {
        return this.itemPrice;
    }
    public String getItemSource() {return this.itemSource;}
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    @NonNull
    @Override
    public Stream<String> stream() {
        return null;
    }

    @NonNull
    @Override
    public Stream<String> parallelStream() {
        return null;
    }
}