package com.activity.chatbot

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity



class QuizplayActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizplay)
        webView = findViewById(R.id.webViewKuis)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl("https://wordwall.net/id/embed/623489a203d2420681409aed8d3beaa7?themeId=1&templateId=46&fontStackId=0")
    }
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            // Kalau web-nya bisa di-back, back web-nya saja
            webView.goBack()
        } else {
            // Kalau sudah mentok, baru tutup Activity-nya
            super.onBackPressed()
        }
    }

}
