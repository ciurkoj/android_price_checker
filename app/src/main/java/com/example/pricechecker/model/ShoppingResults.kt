package com.example.pricechecker.model


/**
 **Shopping Results contains Serp API Json response for shopping response object.
 **It helps to deserialiaze the object and display in a List View.
 **/


data class ShoppingResults(
    private val position: Int,
    private val title: String,
    private val link: String,
    private val product_link: String,
    private val product_id: String,
    private val serpapi_product_api: String,
    private val source: String,
    private val price: String,
    private val extracted_price: Float,
    private val rating: Float,
    private val reviews: Int,
    private val snippet: String,
    private val thumbnail: String
)
