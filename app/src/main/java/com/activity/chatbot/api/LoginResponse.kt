package com.activity.chatbot.api
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,

    @SerializedName("student")
    val student: UserDto? = null,

    @SerializedName("teacher")
    val teacher: UserDto? = null
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,

    @SerializedName("profile_photo_path")
    val profilePhotoPath: String? = null,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)