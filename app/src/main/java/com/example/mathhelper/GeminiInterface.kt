package com.example.mathhelper

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//defines the end to end calls of the api

interface GeminiInterface {
    @GET("gemini/{question}")
    fun generateAnswer(@Path("question") question: String): Call<JsonObject>
}
