package com.example.mathhelper

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "MainActivity"

//CHEETAH
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = RetrofitClient.getInstance()
        val api = retrofit.create(GeminiInterface::class.java)

        enableEdgeToEdge()
        setContent {
            MathHelperTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { handleCameraPermission() },
                            icon = { Icon(Icons.Default.Add, "Extended floating action button.") },
                            text = { Text(text = "Ask with photo") },
                        )
                    }
                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        api = api
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, api: GeminiInterface) {
    var query by rememberSaveable { mutableStateOf("") }
    var answer by rememberSaveable { mutableStateOf("Give Query") }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter Query") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = answer
        )
        Button(
            onClick = {
                //has a lambda for updating answer, and since answer is rememberSaveable, it's change will trigger recomposition
                //was required because solveQuery has a asynchronous call and returns answer delayed.
                solveQuery(query, api) { result ->
                    answer = result
                }
            }
        ) {
            Text("Submit")
        }
    }
}

fun solveQuery(query: String, api: GeminiInterface, updateAnswer: (String) -> Unit) {
    var answer: String = ""
    //this call object is actually a HTTP request, which can then be run synchronously or asynchronously
    //Call<T>, T is the type of response expected from the server
    val call: Call<GeminiResponse> = api.generateAnswer(query)

    //synchronous call
    //val response: String = call!!.execute().body().toString()

    //asynchronous call
    call.enqueue(object : Callback<GeminiResponse> {
        override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
            if (response.isSuccessful) {
                updateAnswer(response.body().response)
            } else {
                updateAnswer("Invalid response received from server")
            }
        }

        override fun onFailure(call: Call<GeminiResponse>?, t: Throwable?) {
            t?.printStackTrace()
            Log.e(TAG, "Chud gaye guru")
            updateAnswer("Failed to fetch answer")
        }
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MathHelperTheme {
        Greeting(
            name = "Android",
            api = object : GeminiInterface {
                override fun generateAnswer(question: String): Call<GeminiResponse> {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}