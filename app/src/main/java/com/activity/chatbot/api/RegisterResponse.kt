package com.activity.chatbot.api

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("student")
    val student: UserDto
)