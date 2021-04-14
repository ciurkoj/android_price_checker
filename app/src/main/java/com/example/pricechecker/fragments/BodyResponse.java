package com.example.pricechecker.fragments;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class BodyResponse {
    private JsonObject search_metadata;
    private JsonObject search_parameters;
    private JsonObject search_information;
    private ArrayList filters;
    private List<ShoppingResult> shopping_results;
    private JsonObject pagination;
    private JsonObject serpapi_pagination;

    public BodyResponse(JsonObject search_metadata,
                        JsonObject search_parameters,
                        JsonObject search_information,
                        ArrayList filters,
                        ArrayList shopping_results,
                        JsonObject pagination,
                        JsonObject serpapi_pagination) {
        this.search_metadata = search_metadata;
        this.search_information = search_information;
        this.filters = filters ;
        this.shopping_results = shopping_results;
        this.pagination = pagination;
        this.serpapi_pagination = serpapi_pagination;


    }

    public JsonObject getSearch_metadata() {
        return search_metadata;
    }

    public JsonObject getSearch_parameters() {
        return search_parameters;
    }

    public JsonObject getSearch_information() {
        return search_information;
    }

    public List<ShoppingResult>  getShopping_results() {
        return this.shopping_results;
    }

    public JsonObject getErrorBody() {
        return serpapi_pagination;
    }
}