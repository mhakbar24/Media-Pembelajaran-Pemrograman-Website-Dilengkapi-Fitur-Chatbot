package com.activity.chatbot.api

import com.google.gson.annotations.SerializedName

data class UpdateStudentRequest(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String? = null
)

data class MessageResponse(
    val message: String
)

