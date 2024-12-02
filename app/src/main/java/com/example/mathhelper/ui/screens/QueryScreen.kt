package com.example.mathhelper.ui.screens

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
import androidx.navigation.NavHostController
import com.example.mathhelper.gemini.GeminiInterface
import com.example.mathhelper.gemini.GeminiResponse
import com.example.mathhelper.ui.theme.MathHelperTheme
import com.example.mathhelper.viewmodel.QueryViewModel
import retrofit2.Call

@Composable
fun QueryScreen(navController: NavHostController, viewModel: QueryViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
//                    openCamera()
                },
                icon = { Icon(Icons.Default.Add, "Extended floating action button.") },
                text = { Text(text = "Ask with photo") },
            )
        }
    ) { innerPadding ->
        Query(
            modifier = Modifier.padding(innerPadding),
            viewModel
        )
    }
}


@Composable
fun Query(
    modifier: Modifier = Modifier,
    viewModel: QueryViewModel
) {
    val api = viewModel.api
    val query by viewModel.query
    val answer by viewModel.answer

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = query,
            onValueChange = { viewModel.updateQuery(it) },
            label = { Text("Enter Query") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = answer
        )
        Button(
            onClick = {
                viewModel.solveQuery()
            }
        ) {
            Text("Submit")
        }
    }
}

//private fun openCamera() {
//    //this checks (and asks if not given) camera permission and launches camera
//    BaseActivity().handleCameraPermission()
//}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    MathHelperTheme {
//        Query(
//            api = object : GeminiInterface {
//                override fun generateAnswer(question: String): Call<GeminiResponse> {
//                    TODO("Not yet implemented")
//                }
//            },
//        )
//    }
//}