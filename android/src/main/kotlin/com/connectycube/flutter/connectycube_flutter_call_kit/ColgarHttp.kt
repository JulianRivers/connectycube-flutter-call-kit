package com.connectycube.flutter.connectycube_flutter_call_kit

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject

fun colgarHttp(sessionID: String, platform: String, recipientId: Int, token: String) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = OkHttpClient()

        val url = "https://api.connectycube.com/calls/reject"

        val json = JSONObject().apply {
            put("recipientId", recipientId)
            put("sessionID", sessionID)
            put("platform", platform)
        }
        Log.d("HTTP_REQUEST JSON", json.toString())

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("CB-Token", token)
                .post(requestBody)
                .build()

        try {
            // Ejecutar la solicitud en el hilo de fondo
            val response: Response = client.newCall(request).execute()
            Log.d("HTTP_REQUEST RESPONSE", "$response")
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("HTTP_REQUEST", "Respuesta exitosa: $responseBody")
            } else {
                Log.d("HTTP_REQUEST", "Error: ${response.code}, ${response.message}")
            }
        } catch (e: Exception) {
            Log.d("HTTP_REQUEST", "Excepción: ${e.message}", e)
        }
    }
}
