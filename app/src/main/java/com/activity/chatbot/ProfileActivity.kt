package com.activity.chatbot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.activity.chatbot.api.ApiClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileActivity : AppCompatActivity() {

    // Sesuaikan ID dengan layout estetik yang baru
    private lateinit var tvNameLarge: TextView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailEmail: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var progress: ProgressBar
    private lateinit var btnLogout: Button // Atau AppCompatButton sesuai layout

    private val api by lazy { ApiClient.apiService(this) }
    private lateinit var session: SessionManager
    private var profileLoadedAtLeastOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        session = SessionManager(this)

        // Proteksi: kalau belum login, balik login
        if (!session.isLoggedIn()) {
            goToLogin()
            return
        }

        // Inisialisasi ID sesuai desain baru
        tvNameLarge = findViewById(R.id.tvProfileNameLarge) // Nama di bawah foto
        tvDetailName = findViewById(R.id.tvDetailName)      // Nama di dalam Card
        tvDetailEmail = findViewById(R.id.tvDetailEmail)    // Email di dalam Card
        tvCreatedAt = findViewById(R.id.tvCreatedAt)        // Tambahkan ID ini di XML jika ingin nampil
        progress = findViewById(R.id.progress)
        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener { doLogout() }

        loadProfile()
    }

    private fun doLogout() {
        lifecycleScope.launch {
            runCatching { api.logoutStudent() }
            session.logout()
            Toast.makeText(this@ProfileActivity, "Berhasil keluar", Toast.LENGTH_SHORT).show()
            goToLogin()
        }
    }

    private fun loadProfile() {
        setLoading(true)

        lifecycleScope.launch {
            try {
                val profile = api.getStudentProfile()

                // Masukkan data ke UI baru
                tvNameLarge.text = profile.name
                tvDetailName.text = profile.name
                tvDetailEmail.text = profile.email
                val registeredAt = profile.createdAt?.substringBefore("T") ?: "-"
                tvCreatedAt.text = "Terdaftar sejak: $registeredAt"

                // Update session biar kalau pindah page, datanya tetap terbaru
                val token = session.getToken()
                if (!token.isNullOrBlank()) {
                    session.saveSession(
                        token = token,
                        role = session.getRole() ?: "siswa",
                        userId = profile.id,
                        name = profile.name,
                        email = profile.email
                    )
                }

                loadProgressSummary(registeredAt)
                profileLoadedAtLeastOnce = true

            } catch (e: Exception) {
                handleApiError(e)
            } finally {
                setLoading(false)
            }
        }
    }

    private suspend fun loadProgressSummary(registeredAt: String) {
        val overview = runCatching { api.getProgressOverview() }.getOrNull()
        val ai = runCatching { api.generateProgressAi() }.getOrNull()

        val quizLine = overview?.quizProgress?.let {
            "Quiz avg ${it.averageScore}, terbaik ${it.bestScore}, trend ${it.trend}"
        } ?: "Progress quiz belum tersedia"

        val materiLine = overview?.materiProgress?.let {
            "Cakupan materi ${it.coveragePercent}% (${it.uniqueMateriAccessed}/${it.totalMateriAvailable})"
        } ?: "Progress materi belum tersedia"

        val aiLine = ai?.analysisSections?.ringkasan
            ?: ai?.analysis
            ?: "Analisis AI belum tersedia"

        tvCreatedAt.text = "Terdaftar sejak: $registeredAt\n$quizLine\n$materiLine\nAI: $aiLine"
    }

    private fun handleApiError(error: Exception) {
        if (error is HttpException && error.code() == 401) {
            session.logout()
            Toast.makeText(this, "Sesi berakhir, silakan login lagi.", Toast.LENGTH_SHORT).show()
            goToLogin()
            return
        }

        // Saat refresh ulang profile, error jaringan tidak perlu memaksa logout.
        val message = if (profileLoadedAtLeastOnce) {
            "Gagal memperbarui profil. Coba lagi nanti."
        } else {
            "Koneksi ke server bermasalah"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setLoading(loading: Boolean) {
        progress.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun goToLogin() {
        startActivity(Intent(this@ProfileActivity, LoginFormActivity::class.java))
        finishAffinity() // Menghapus semua tumpukan activity agar tidak bisa "Back"
    }

}