package com.activity.chatbot.api

import org.json.JSONObject
import retrofit2.HttpException

object ApiErrorParser {
    fun getReadableMessage(httpException: HttpException, fallback: String): String {
        val errorBody = httpException.response()?.errorBody()?.string().orEmpty()
        if (errorBody.isBlank()) return fallback

        return runCatching {
            val json = JSONObject(errorBody)
            val message = json.optString("message")
            if (message.isNotBlank()) {
                return@runCatching message
            }

            val errors = json.optJSONObject("errors")
            if (errors != null) {
                val keys = errors.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val firstError = errors.optJSONArray(key)?.optString(0).orEmpty()
                    if (firstError.isNotBlank()) {
                        return@runCatching firstError
                    }
                }
            }

            fallback
        }.getOrDefault(fallback)
    }
}
