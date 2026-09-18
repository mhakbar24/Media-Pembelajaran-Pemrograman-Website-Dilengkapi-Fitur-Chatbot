package com.activity.chatbot
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_info)

        val pengembang = findViewById<CardView>(R.id.cardProfil)
        pengembang.setOnClickListener {
            startActivity(Intent(this@InfoActivity, PengembangActivity::class.java))
        }
            val petunjuk = findViewById<CardView>(R.id.cardPenggunaan)
            petunjuk.setOnClickListener {
                startActivity(Intent(this@InfoActivity, PenggunaanActivity::class.java))
            }


    }
}

