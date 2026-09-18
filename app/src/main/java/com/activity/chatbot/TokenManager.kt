package com.activity.chatbot
import android.content.Context

class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveAuthHeader(authHeader: String) {
        prefs.edit().putString("auth_header", authHeader).apply()
    }

    fun getAuthHeader(): String? = prefs.getString("auth_header", null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}