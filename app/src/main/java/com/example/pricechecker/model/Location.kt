package com.example.pricechecker.model

data class Location (
    val name: String,
    val canonical_name: String,
    val gps: ArrayList<Double>
)
