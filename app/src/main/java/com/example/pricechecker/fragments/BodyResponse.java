package com.example.pricechecker.fragments;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Body response gathers the information from Json API response.
 * The class can be extended to handle more elements such as "search_metadata",
 * however at this point there is no need to initialise more variables.
 */
public class BodyResponse {

    private List<ShoppingResult> shopping_results;
    private List<LocalResult> local_results;


    /**
     *
     * @param shopping_results
     * @param local_results
     */
    public BodyResponse(
                        ArrayList shopping_results,
                        List<LocalResult> local_results)
    {
        this.shopping_results = shopping_results;
        this.local_results = local_results;
    }
    /**
     *
     * @return
     */
    public List<ShoppingResult> getShopping_results() {
        return this.shopping_results;
    }

    /**
     *
     * @return
     */
    public List<LocalResult> getLocal_results() {return this.local_results;}


}