package com.example.mathhelper.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TAG = "CameraX"

object CameraUtils {

    fun createImageCapture(): ImageCapture = ImageCapture.Builder().build()

    fun createPreview(): Preview = Preview.Builder().build()

    fun createCameraSelector(): CameraSelector =
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

    suspend fun bindCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector,
        preview: Preview,
        imageCapture: ImageCapture,
        previewView: PreviewView
    ) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        preview.surfaceProvider = previewView.surfaceProvider
    }


//    fun analyzeImage() {
//        val imageAnalysis = ImageAnalysis.Builder()
//            // enable the following line if RGBA output is needed.
//            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
//            .setTargetResolution(Size(1280, 720))
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//        imageAnalysis.setAnalyzer(executor, ImageAnalysis.Analyzer { imageProxy ->
//            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
//            // insert your code here.
//            //...
//            // after done, release the ImageProxy object
//            imageProxy.close()
//        })
//
//        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)
//    }

    fun captureImage(imageCapture: ImageCapture, context: Context, onImageSaved: (String) -> Unit) {
        val name = "CameraxImage_${System.currentTimeMillis()}.jpeg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.i(TAG, "Image Saved Successfully at ${outputFileResults.savedUri.toString()}")
                    onImageSaved(outputFileResults.savedUri.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Image Save Failed: $exception")
                }
            }
        )
    }

    private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
        suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(this).also { provider ->
                provider.addListener(
                    { continuation.resume(provider.get()) },
                    ContextCompat.getMainExecutor(this)
                )
            }
        }
}
