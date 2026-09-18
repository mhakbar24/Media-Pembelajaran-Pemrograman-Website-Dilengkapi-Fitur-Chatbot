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
import com.activity.chatbot.api.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: LinearLayout
    private lateinit var tvBtn: TextView
    private lateinit var progress: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tvToLogin: TextView

    private val api by lazy { ApiClient.apiService(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvBtn = findViewById(R.id.tvBtn)
        progress = findViewById(R.id.progress)
        tvError = findViewById(R.id.tvError)
        tvToLogin = findViewById(R.id.tvToLogin)

        btnRegister.setOnClickListener { doRegister() }
        tvToLogin.setOnClickListener { goToLogin() }
    }

    private fun doRegister() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            showError("Nama, email, dan password wajib diisi.")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Format email tidak valid.")
            return
        }
        if (password.length < 8) {
            showError("Password minimal 8 karakter.")
            return
        }

        hideError()
        setLoading(true)

        lifecycleScope.launch {
            try {
                val res = api.registerStudent(RegisterRequest(name, email, password))

                // Register backend kamu tidak mengembalikan token, jadi arahkan ke login
                val i = Intent(this@RegisterActivity, LoginFormActivity::class.java)
                i.putExtra("prefill_email", res.student.email)
                startActivity(i)
                finish()

            } catch (e: HttpException) {
                val fallback = when (e.code()) {
                    422 -> "Data pendaftaran tidak valid."
                    else -> "Gagal daftar. (${e.code()})"
                }
                showError(ApiErrorParser.getReadableMessage(e, fallback))
            } catch (e: Exception) {
                showError("Gagal daftar. Cek koneksi internet.")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginFormActivity::class.java))
        finish()
    }

    private fun setLoading(loading: Boolean) {
        btnRegister.isEnabled = !loading
        btnRegister.alpha = if (loading) 0.7f else 1f
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        tvBtn.text = if (loading) "Memproses..." else "Daftar"
    }

    private fun showError(msg: String) {
        tvError.text = msg
        tvError.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvError.visibility = View.GONE
    }
}