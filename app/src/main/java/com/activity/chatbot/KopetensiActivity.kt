package com.activity.chatbot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class KopetensiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kopetensi)
        findViewById<Button>(R.id.btnMengerti).setOnClickListener {
            finish() // Kembali ke Main Menu
        }
    }
}