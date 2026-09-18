package com.activity.chatbot.api

import android.content.Context
import com.activity.chatbot.BuildConfig
import com.activity.chatbot.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://fansnime.my.id/api/"

    // Retrofit dibuat dari OkHttp yang sudah ada AuthInterceptor
    private fun provideOkHttp(context: Context): OkHttpClient {
        val session = SessionManager(context)

        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(session))

        // Logging hanya saat debug
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    private fun provideRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttp(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun apiService(context: Context): ApiService {
        return provideRetrofit(context).create(ApiService::class.java)
    }

    fun create(sessionManager: SessionManager, baseUrl: String): ApiService {
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(normalizeBaseUrl(baseUrl))
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    private fun normalizeBaseUrl(baseUrl: String): String {
        val trimmed = baseUrl.trim().trimEnd('/')
        return if (trimmed.endsWith("/api")) "$trimmed/" else "$trimmed/api/"
    }
}