package com.example.pricechecker.fragments;

public class ImageResult {
    private String title;
    private int position;

    public ImageResult(String title,
                        int position) {
        this.title = title;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }
    public int getPosition() { return position; }
}
