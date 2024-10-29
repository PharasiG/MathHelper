package com.example.mathhelper

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mathhelper.ui.theme.MathHelperTheme
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = RetrofitClient.getInstance()
        val api: GeminiInterface = retrofit.create(GeminiInterface::class.java)
        val call: Call<JsonObject> = api.generateAnswer("1 + 11")

//        synchronous call
//        val response: String = call!!.execute().body().toString()

        //asynchronous call
        var answer by mutableStateOf("Give Query")
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    answer = response.body().toString()
                } else {
                    answer = "Failed to generate response"
                }
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                t?.printStackTrace()
                Log.e(TAG, "Chud gaye guru")
            }
        })

        enableEdgeToEdge()
        setContent {
            MathHelperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        response = answer
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, response: String) {
    var text by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter Query") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = response
        )
        Button( onClick = {} ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MathHelperTheme {
        Greeting("Android", response = "Sample Response")
    }
}