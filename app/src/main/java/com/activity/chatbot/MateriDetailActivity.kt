package com.activity.chatbot
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.ApiService
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class MateriDetailActivity : AppCompatActivity() {

    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_detail)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        val id = intent.getIntExtra("material_id", -1)
        if (id == -1) {
            Toast.makeText(this, "ID materi tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        api = ApiClient.apiService(this)

        loadDetail(id)
    }

    private fun loadDetail(id: Int) {
        lifecycleScope.launch {
            try {
                val res = api.getMaterialDetail(id)
                val item = res.data

                findViewById<TextView>(R.id.tvTitle).text = item.title
                findViewById<TextView>(R.id.tvCategory).text = "Kategori: ${item.category}"
                findViewById<TextView>(R.id.tvTeacher).text =
                    "Guru: ${item.teacher?.name ?: "-"} (${item.teacher?.email ?: "-"})"
                findViewById<TextView>(R.id.tvDate).text = "Tanggal: ${item.created_at ?: "-"}"
                findViewById<TextView>(R.id.tvDescription).text = item.description ?: "-"

                val img = findViewById<ImageView>(R.id.imgMateri)
                if (!item.image.isNullOrEmpty()) {
                    img.visibility = View.VISIBLE
                    Glide.with(this@MateriDetailActivity)
                        .load(item.image)
                        .into(img)
                } else {
                    img.visibility = View.GONE
                }

                // Tracking akses materi tidak boleh mengganggu render halaman detail.
                runCatching { api.trackMaterialAccess(id) }

            } catch (e: Exception) {
                Toast.makeText(this@MateriDetailActivity, "Gagal memuat detail: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}