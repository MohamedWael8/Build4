package com.itworx.osfoora

import android.app.DownloadManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.*
import osfora.itworx.com.osfora.data_controller.SearchDataController
import osfora.itworx.com.osfora.data_controller.web_service.Oauth1SigningInterceptor
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient



fun makeApiCall(url: String, successResponseAction: (response: String)-> Unit, failedResponseAction: (searchError: SearchDataController.SearchError)-> Unit) 
{
    val random = Random()
    val oauth1 = Oauth1SigningInterceptor.Builder()
        .consumerKey("9G6y5Zf6DdMxLK4nTE5nEV96e")
        .consumerSecret("pZpNntftG4cLwTAOMRleLGXWnucQpdq1fTPCYxfVk7SCJPh5Wx")
        .accessToken("348414445-0ZYFcZQEzV3g3XYncjYqpZUdM5OMrKCVyk6BKpBN")
        .accessSecret("i5h6KRJT9EuTfEeb5xJj0iOBc5XS8rLnCBLDGn2yQ2GgC")
        .random(random)
        .build()
    val request = Request.Builder()
        .url(url)
        .build()
    val signed = oauth1.signRequest(request)
    val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()
    client.newCall(signed).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            Handler(Looper.getMainLooper()).post {
                successResponseAction(response.body()?.string().toString())            }
            }
        override fun onFailure(call: Call, e: IOException) {
            Handler(Looper.getMainLooper()).post {
                if (e is java.net.ConnectException) {
                    failedResponseAction(SearchDataController.SearchError.ERROR_TIMEOUT)
                } else {
                    failedResponseAction(SearchDataController.SearchError.ERROR_SERVER)
                }
            }
        }
    })
}