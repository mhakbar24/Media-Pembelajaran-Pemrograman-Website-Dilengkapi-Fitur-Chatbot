package com.activity.chatbot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.ApiErrorParser
import com.activity.chatbot.api.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginFormActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: LinearLayout
    private lateinit var tvBtn: TextView
    private lateinit var progress: ProgressBar
    private lateinit var tvError: TextView

    private val api by lazy { ApiClient.apiService(this) }
    private lateinit var session: SessionManager

    // kalau kamu punya pilihan role, nanti kita ambil dari intent
    private var role: String = "siswa" // default siswa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)
        findViewById<TextView>(R.id.tvRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        session = SessionManager(this)

        // AUTO SKIP LOGIN
        if (session.isLoggedIn()) {
            goToMain(session.getRole() ?: "siswa", session.getName() ?: "User")
            return
        }

        // OPTIONAL: role dari screen sebelumnya (kalau ada)
        role = intent.getStringExtra("role") ?: "siswa"

        // Bind views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvBtn = findViewById(R.id.tvBtn)
        progress = findViewById(R.id.progress)
        tvError = findViewById(R.id.tvError)

        // Jika datang dari halaman daftar, bantu isi email otomatis.
        etEmail.setText(intent.getStringExtra("prefill_email").orEmpty())

        btnLogin.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isBlank() || password.isBlank()) {
            showError("Email dan password wajib diisi.")
            return
        }

        hideError()
        setLoading(true)

        lifecycleScope.launch {
            try {
                val req = LoginRequest(email, password)

                val res = if (role == "teacher") {
                    api.loginTeacher(req)
                } else {
                    api.loginSiswa(req) // siswa
                }

                val user = res.student ?: res.teacher
                if (res.token.isBlank()) {
                    showError("Login gagal: token tidak ditemukan.")
                    return@launch
                }
                if (user == null) {
                    showError("Response server tidak valid (user null).")
                    return@launch
                }

                val savedRole = if (res.student != null) "siswa" else "teacher"

                session.saveSession(
                    token = res.token,
                    role = savedRole,
                    userId = user.id,
                    name = user.name,
                    email = user.email
                )

                goToMain(savedRole, user.name)

            } catch (e: HttpException) {
                val fallback = when (e.code()) {
                    401 -> "Email atau password salah."
                    422 -> "Data login tidak valid."
                    else -> "Server error (${e.code()})."
                }
                showError(ApiErrorParser.getReadableMessage(e, fallback))
            } catch (e: Exception) {
                showError("Login gagal. Cek koneksi internet.")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun goToMain(role: String, name: String) {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("role", role)
        i.putExtra("name", name)
        startActivity(i)
        finish()
    }

    private fun setLoading(loading: Boolean) {
        btnLogin.isEnabled = !loading
        btnLogin.alpha = if (loading) 0.7f else 1f
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        tvBtn.text = if (loading) "Memproses..." else "Masuk Aplikasi"
    }

    private fun showError(msg: String) {
        tvError.text = msg
        tvError.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvError.visibility = View.GONE
    }

}