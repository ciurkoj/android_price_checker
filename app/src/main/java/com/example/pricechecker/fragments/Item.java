package com.example.pricechecker.fragments;

public class Item {
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

}