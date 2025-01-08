package com.example.mathhelper.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

const val TAGocr = "OcrUtils"

object OcrUtils {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImage(context: Context, uri: String, onImageProcessed: (String) -> Unit) {
        val image: InputImage
        try {
            val imageUri: Uri = Uri.parse(uri)
            image = InputImage.fromFilePath(context, imageUri)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    Log.i(TAGocr, visionText.text)
                    onImageProcessed(visionText.text)
                }
                .addOnFailureListener { e ->
                    Log.i(TAGocr, "Recognition Failed")
                    e.printStackTrace()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}