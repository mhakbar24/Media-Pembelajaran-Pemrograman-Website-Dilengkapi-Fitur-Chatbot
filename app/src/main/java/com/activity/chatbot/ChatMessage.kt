package com.activity.chatbot

data class ChatMessage(
    val text: String,
    val isUser: Boolean // true = pesan dari User, false = pesan dari Bot Gemini
)