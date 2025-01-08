package com.example.mathhelper.ui.screens

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.mathhelper.navigation.NavigationItem
import com.example.mathhelper.utils.OcrUtils.processImage
import com.example.mathhelper.viewmodel.QueryViewModel

@Composable
fun ImagePreviewScreen(navController: NavHostController, uri: String, viewModel: QueryViewModel) {

    val context = LocalContext.current
    var analyzedText: String? = null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Log.d("ImagePreviewScreen", "Image URI: $uri")

        // Display the captured image
        AsyncImage(
            model = uri,
            contentDescription = "Captured Image",
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Spacing between icons
        ) {
            // Discard button with ripple effect
            Box(
                modifier = Modifier
                    .size(60.dp) // Ensure enough space for ripple effect
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = Color.White)
                    ) {
                        navController.navigateUp()
//                        navController.navigate(NavigationItem.Camera.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Discard",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            // Select button with ripple effect
            Box(
                modifier = Modifier
                    .size(60.dp) // Ensure enough space for ripple effect
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = Color.White)
                    ) {
                        processImage(
                            context,
                            uri,
                            onImageProcessed = {
                                analyzedText = it
                                viewModel.updateQuery(analyzedText!!)
                                navController.popBackStack(NavigationItem.Query.route, false)
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Select",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ImagePreviewScreenPreview() {
    ImagePreviewScreen(
        navController = NavHostController(LocalContext.current),
        uri = "https://uploads.vibra.co/1/2023/11/significado-espiritual-de-los-girasoles.jpg",
        QueryViewModel()
    )
}