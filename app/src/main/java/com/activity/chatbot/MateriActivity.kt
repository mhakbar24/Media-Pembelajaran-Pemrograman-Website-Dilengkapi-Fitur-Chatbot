package com.activity.chatbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MateriActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi)

        val metpers = findViewById<ImageView>(R.id.metper)
        metpers.setOnClickListener{
            startActivity(Intent(this@MateriActivity,PerawatanActivity::class.java))
        }
        val perhards = findViewById<ImageView>(R.id.perhard)
        perhards.setOnClickListener{
            startActivity(Intent(this@MateriActivity,PerawatanhardActivity::class.java))
        }


        val toolkits = findViewById<ImageView>(R.id.toolkit)
        toolkits.setOnClickListener{
            startActivity(Intent(this@MateriActivity,ToolskitActivity::class.java))
        }
        val prosedurs = findViewById<ImageView>(R.id.prosedur)
        prosedurs.setOnClickListener{
            startActivity(Intent(this@MateriActivity,ProsedurActivity::class.java))
        }
        val troubles = findViewById<ImageView>(R.id.trouble)
        troubles.setOnClickListener{
            startActivity(Intent(this@MateriActivity,TroubleActivity::class.java))
        }
    }
}