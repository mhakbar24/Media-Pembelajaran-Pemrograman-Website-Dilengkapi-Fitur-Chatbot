package com.activity.chatbot.api

import com.google.gson.annotations.SerializedName

data class MaterialItem(
    val id: Int,
    val title: String,
    val category: String,
    @SerializedName("isi_materi")
    val isiMateri: String? = null,
    val description: String?,
    val image: String?, // null di JSON
    val teacher: Teacher?,
    val created_at: String?
)
