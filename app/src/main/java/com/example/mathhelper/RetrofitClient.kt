package com.example.mathhelper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//this object ensures that only one instance of retrofit is created and thus only one
//connection is established with the server at a time

object RetrofitClient {
    fun getInstance(): Retrofit {
//        for emulator
//        val BASE_URL = "http://10.0.2.2:5000"

//        for device
        val BASE_URL = "http://zaurav.centralindia.cloudapp.azure.com"

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        return retrofit
    }
}