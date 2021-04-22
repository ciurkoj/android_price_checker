package com.example.pricechecker.fragments;

import com.google.gson.JsonObject;

import java.util.List;

;

public class LocalResult {
    private int position;
    private String title;
    private String place_id;
    private String lsig;
    private String place_id_search;
    private String rating;
    private JsonObject links;
    private String address;
    private JsonObject service_options;
    private JsonObject gps_coordinates;

    public LocalResult(int position, String title,
                       String place_id,
                       String lsig,
                       String place_id_search,
                       String rating,
                       JsonObject links,
                       String address,
                       JsonObject service_options,
                       JsonObject gps_coordinates
    ) {
        this.position = position;
        this.title = title;
        this.place_id = place_id;
        this.lsig = lsig;
        this.place_id_search = place_id_search;
        this.rating = rating;
        this.links = links;
        this.address = address;
        this.service_options = service_options;
        this.gps_coordinates = gps_coordinates;


    }

    public String getResultTitle() {
        return title;
    }

    public String getResultLsig() {
        return lsig;
    }

    public String getResultPlaceIdSearch() {
        return place_id_search;
    }

    public String getResultAddress() {
        return address;
    }

    public JsonObject getGpsCoordinates() { return gps_coordinates; }

}
