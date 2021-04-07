package com.example.pricechecker.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class SerpApiQuery(
    @SerializedName("search_metadata")
        val search_metadata: JsonObject,
        val search_parameters: JsonObject,
        val search_information: JsonObject,
        val recipes_results: JsonArray,
        val local_map: JsonObject,
        val local_result: JsonObject,
        val knowledge_graph: JsonObject,
        val related_questions: JsonArray,
        val organic_results: JsonArray,
        val related_search_boxes: JsonArray,
        val related_searches: JsonArray,
        val pagination: JsonObject,
        val serpapi_pagination: JsonObject


    )
