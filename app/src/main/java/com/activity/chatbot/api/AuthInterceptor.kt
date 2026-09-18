package com.activity.chatbot.api

import com.activity.chatbot.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getToken()
        val reqBuilder = chain.request().newBuilder()
            .header("Accept", "application/json")

        if (!token.isNullOrBlank()) {
            reqBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(reqBuilder.build())
    }
}