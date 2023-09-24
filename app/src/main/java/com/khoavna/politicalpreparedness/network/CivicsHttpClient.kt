package com.khoavna.politicalpreparedness.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient : OkHttpClient() {

    companion object {

        private const val API_KEY = "AIzaSyDqFCObDWO3BGB9hpOsGeZZbxdCtQwQxs0"

        fun getClient(): OkHttpClient {
            return Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original.url
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }
                .build()
        }

    }

}