package com.example.calllogger

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object ApiClient {
    private val client = OkHttpClient()

    fun send(url: String, data: Map<String, String>) {
        try {
            val json = JSONObject(data as Map<*, *>).toString()
            val body = json.toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
            client.newCall(request).execute().close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
