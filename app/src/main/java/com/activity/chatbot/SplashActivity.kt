package com.activity.chatbot
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = android.os.Handler().also {
            it.postDelayed({
                startActivity(Intent(this, LoginFormActivity::class.java))
                finish()}, 2000L)
        }
    }
}