package com.example.mathhelper.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mathhelper.TAG
import com.example.mathhelper.gemini.GeminiInterface
import com.example.mathhelper.gemini.GeminiResponse
import com.example.mathhelper.gemini.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueryViewModel : ViewModel() {
    //api initialization
    private val retrofit = RetrofitClient.getInstance()
    private val api = retrofit.create(GeminiInterface::class.java)

    private val _query = mutableStateOf("")
    val query: State<String> = _query

    private val _answer = mutableStateOf("Give Query")
    val answer: State<String> = _answer


    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun solveQuery() {
        //this call object is actually a HTTP request, which can then be run synchronously or asynchronously
        //Call<T>, T is the type of response expected from the server
        val call: Call<GeminiResponse> = api.generateAnswer(_query.value)

        //synchronous call
        //val response: String = call!!.execute().body().toString()

        //asynchronous call
        call.enqueue(object : Callback<GeminiResponse> {
            override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
                if (response.isSuccessful) {
                    _answer.value = response.body().response
                } else {
                    _answer.value = "Invalid response received from server"
                }
            }

            override fun onFailure(call: Call<GeminiResponse>?, t: Throwable?) {
                t?.printStackTrace()
                Log.e(TAG, "Chud gaye guru")
                _answer.value = "Failed to fetch answer"
            }
        })
    }
}