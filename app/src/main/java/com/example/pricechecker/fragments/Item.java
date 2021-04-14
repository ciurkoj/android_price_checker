package com.example.pricechecker.fragments;

public class Item {
    private String itemName;
    private String itemDescription;
    private String thumbnailUrl;

    public Item(String name, String description, String thumbnailUrl) {
        this.itemName = name;
        this.itemDescription = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getItemDescription() {
        return this.itemDescription;
    }
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

}