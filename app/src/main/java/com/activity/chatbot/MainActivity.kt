package com.activity.chatbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.activity.chatbot.api.ApiClient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val api by lazy { ApiClient.apiService(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvHi = findViewById<TextView>(R.id.hibottext)
        tvHi.text = "Halo, Dummy siswa! 👋\nKamu siap untuk belajar pemrograman dengan menyenangkan?"
        lifecycleScope.launch {
            try {
                val profile = api.getStudentProfile()
                tvHi.text =
                    "Halo, ${profile.name}! 👋\nKamu siap untuk belajar pemrograman dengan menyenangkan?"
            } catch (e: Exception) {
                // kalau error, biarkan default text
            }
            val materis = findViewById<ImageView>(R.id.materi)
            materis.setOnClickListener {
                startActivity(Intent(this@MainActivity, MateriActivity2::class.java))
            }
            val quiz = findViewById<ImageView>(R.id.kuis)
            quiz.setOnClickListener {
                startActivity(Intent(this@MainActivity, KuisActivity::class.java))
            }
            val studi = findViewById<ImageView>(R.id.benner)
            studi.setOnClickListener {
                startActivity(Intent(this@MainActivity, KopetensiActivity::class.java))
            }
            val teach = findViewById<ImageView>(R.id.bot)
            teach.setOnClickListener {
                startActivity(Intent(this@MainActivity, ChatbotActivity::class.java))
            }

            val hibot = findViewById<ImageView>(R.id.hibot)
            hibot.setOnClickListener {
                startActivity(Intent(this@MainActivity, ChatbotActivity::class.java))
            }
            val info = findViewById<ImageView>(R.id.info)
            info.setOnClickListener {
                startActivity(Intent(this@MainActivity, InfoActivity::class.java))
            }
            val profil = findViewById<ImageView>(R.id.profile)
            profil.setOnClickListener {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            }

        }
    }
}
