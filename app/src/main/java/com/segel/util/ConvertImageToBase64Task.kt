package com.segel.util

/*
class ConvertImageToBase64Task {
}*/
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ConvertImageToBase64Task : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String? {
        val imageUrl = params[0]

        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byteArray = baos.toByteArray()

            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(base64Image: String?) {
        if (base64Image != null) {
        } else {
            // Handle the error
        }
    }
}
