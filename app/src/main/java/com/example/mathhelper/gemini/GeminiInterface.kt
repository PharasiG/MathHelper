package com.example.mathhelper.gemini

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//defines the end to end calls of the api
interface GeminiInterface {
    @GET("gemini/{question}")
    fun generateAnswer(@Path("question") question: String): Call<GeminiResponse>
}


//some annotation can also be done for better results in complex scenarios (not sure)
data class GeminiResponse(
    val response: String
)