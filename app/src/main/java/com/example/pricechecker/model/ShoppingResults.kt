package com.example.pricechecker.model

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
