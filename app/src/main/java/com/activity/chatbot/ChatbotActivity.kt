package com.activity.chatbot

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.activity.chatbot.api.ApiClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatbotActivity : AppCompatActivity() {
    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton

    private val chatList = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    // Inisialisasi API & Nama Default
    private val api by lazy { ApiClient.apiService(this) }
    private var namaSiswa: String = "Sobat Coder"

    // Model Gemini dibuat lateinit karena menunggu nama dari API
    private lateinit var generativeModel: GenerativeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        rvChat = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        // 1. Setup RecyclerView
        chatAdapter = ChatAdapter(chatList)
        rvChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // Agar chat otomatis ke bawah saat keyboard muncul
        }
        rvChat.adapter = chatAdapter

        // 2. Ambil Profil via API Laravel
        lifecycleScope.launch {
            try {
                val profile = api.getStudentProfile()
                namaSiswa = profile.name
            } catch (e: Exception) {
                // Jika gagal, biarkan namaSiswa tetap "Sobat Coder"
            } finally {
                // 3. Apapun hasilnya, siapkan Gemini & Tampilkan Salam Pembuka
                setupGemini()
                addMessageToScreen(
                    "Halo $namaSiswa! 🚀 Bapak siap bantu kamu belajar hari ini. Apa yang mau kita bahas?",
                    false
                )
            }
        }

        btnSend.setOnClickListener {
            val userText = etMessage.text.toString().trim()
            if (userText.isNotEmpty()) {
                // Pastikan Gemini sudah siap sebelum kirim pesan
                if (::generativeModel.isInitialized) {
                    addMessageToScreen(userText, true)
                    etMessage.text.clear()
                    sendMessageToGemini(userText)
                } else {
                    Toast.makeText(this, "Sabar ya, Pak Guru lagi bersiap...", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setupGemini() {
        generativeModel = GenerativeModel(
            modelName = "gemini-3-flash-preview",
            apiKey = "AIzaSyBnnsLd6wsOPfZ-o_RCXKkJ78C6XvEWza0", // <-- Masukkan API Key-mu di sini
            systemInstruction = content {
                text(
                    "Kamu adalah Pak Guru AI yang ramah. Nama siswamu adalah $namaSiswa. " +
                            "Tugasmu membantu belajar HTML, CSS, JS, dan PHP. Sapa $namaSiswa sesekali."
                )
            }
        )
    }

    private fun addMessageToScreen(message: String, isUser: Boolean) {
        chatList.add(ChatMessage(message, isUser))
        chatAdapter.notifyItemInserted(chatList.size - 1)
        rvChat.smoothScrollToPosition(chatList.size - 1)
    }

    private fun sendMessageToGemini(prompt: String) {
        // Munculkan tulisan mengetik sebelum panggil API
        showTypingIndicator()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(prompt)
                val botReply = response.text ?: "Maaf $namaSiswa, Bapak kurang paham."

                withContext(Dispatchers.Main) {
                    hideTypingIndicator() // HAPUS indikator dulu
                    addMessageToScreen(botReply, false) // BARU munculkan jawaban asli
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideTypingIndicator()
                    Toast.makeText(this@ChatbotActivity, "Koneksi terputus!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showTypingIndicator() {
        chatList.add(ChatMessage("Pak Guru sedang mengetik...", false)) // Pakai teks "..." sebagai penanda
        chatAdapter.notifyItemInserted(chatList.size - 1)
        rvChat.smoothScrollToPosition(chatList.size - 1)
    }

    private fun hideTypingIndicator() {
        if (chatList.isNotEmpty() && chatList.last().text == "Pak Guru sedang mengetik...") {
            val lastIndex = chatList.size - 1
            chatList.removeAt(lastIndex)
            chatAdapter.notifyItemRemoved(lastIndex)
        }
    }
}
