package com.example.mathhelper.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mathhelper.gemini.GeminiInterface
import com.example.mathhelper.gemini.GeminiResponse
import com.example.mathhelper.navigation.NavigationItem
import com.example.mathhelper.ui.theme.MathHelperTheme
import com.example.mathhelper.viewmodel.QueryViewModel

const val TAG = "QUERY_SCREEN"

@Composable
fun QueryScreen(navController: NavHostController, viewModel: QueryViewModel) {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG,"CAMERA PERMISSION GRANTED")
            navController.navigate(NavigationItem.Camera.route)
        } else {
            Log.d(TAG,"CAMERA PERMISSION DENIED")
        }
    }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) -> {
                            navController.navigate(NavigationItem.Camera.route)
                        }
                        else -> {
                            // Asking for permission
                            launcher.launch(Manifest.permission.CAMERA)
                        }
                    }
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


@Preview(showBackground = true)
@Composable
fun QueryScreenPreview() {
    QueryScreen(navController = rememberNavController(), viewModel = QueryViewModel())
}