package com.activity.chatbot.api
import com.google.gson.annotations.SerializedName

data class StudentProfileResponse(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("profile_photo_path") val profilePhotoPath: String? = null,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)