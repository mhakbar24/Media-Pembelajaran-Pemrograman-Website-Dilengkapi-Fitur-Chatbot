package com.activity.chatbot

import android.content.Context

class SessionManager(context: Context) {

    private val pref = context.getSharedPreferences("teman_belajar_session", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_ROLE = "role"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    // =============================
    // SAVE SESSION
    // =============================
    fun saveSession(
        token: String,
        role: String,
        userId: Int,
        name: String,
        email: String
    ) {
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_ROLE, role)
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    // =============================
    // GETTERS
    // =============================
    fun getToken(): String? = pref.getString(KEY_TOKEN, null)

    fun getRole(): String? = pref.getString(KEY_ROLE, null)

    fun getUserId(): Int = pref.getInt(KEY_USER_ID, -1)

    fun getName(): String? = pref.getString(KEY_NAME, null)

    fun getEmail(): String? = pref.getString(KEY_EMAIL, null)

    fun isLoggedIn(): Boolean = !getToken().isNullOrBlank()

    // =============================
    // CLEAR SESSION (LOGOUT)
    // =============================
    fun logout() {
        editor.clear()
        editor.apply()
    }
}