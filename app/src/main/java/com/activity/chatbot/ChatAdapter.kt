package com.activity.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messageList: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // Fungsi ini menentukan apakah pesan harus pakai desain hijau (User) atau putih (Bot)
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return when {
            message.isUser -> 1
            message.text == "Pak Guru sedang mengetik..." -> 2 // 2 = Indikator Mengetik
            else -> 0                  // 0 = Bot
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layout = when (viewType) {
            1 -> R.layout.item_chat_user // Kanan
            2 -> R.layout.item_chat_bot  // Indikator mengetik (pakai layout bot tapi teksnya ...)
            else -> R.layout.item_chat_bot // Kiri (Pesan bot biasa)
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        // Menempelkan teks pesan ke dalam TextView di XML
        val message = messageList[position]
        holder.tvMessage.text = message.text
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // Menyambungkan ID TextView dari desain XML
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessageItem)
    }
}