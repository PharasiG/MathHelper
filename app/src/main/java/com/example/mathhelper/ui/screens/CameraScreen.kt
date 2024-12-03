package com.example.mathhelper.ui.screens

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.mathhelper.utils.CameraUtils

@Composable
fun CameraScreen(navController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // CameraX components
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { CameraUtils.createImageCapture() }
    val preview = CameraUtils.createPreview()
    val cameraSelector = CameraUtils.createCameraSelector()

    // Camera binding
    LaunchedEffect(Unit) {
        CameraUtils.bindCamera(
            context = context,
            lifecycleOwner = lifecycleOwner,
            cameraSelector = cameraSelector,
            preview = preview,
            imageCapture = imageCapture,
            previewView = previewView
        )
    }

    // UI layout
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        ShutterButton(imageCapture = imageCapture, context = context)
    }
}

@Composable
fun ShutterButton(imageCapture: ImageCapture, context: Context) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(16.dp)
            .size(72.dp)
            .background(color = Color.White, shape = CircleShape)
            .clickable { CameraUtils.captureImage(imageCapture, context) }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color = Color.Gray, shape = CircleShape)
        )
    }
}
