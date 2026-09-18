package com.activity.chatbot

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.ApiService
import kotlinx.coroutines.launch

class DaftarMateriActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var tvJudul: TextView
    private lateinit var adapter: DaftarMateriAdapter
    private lateinit var api: ApiService
    private lateinit var sessionManager: SessionManager

    // Variabel untuk menyimpan kategori apa yang sedang dibuka
    private var kategoriTerpilih: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_materi)

        // 1. Tangkap nama kategori dari halaman Menu Utama (MateriActivity2)
        kategoriTerpilih = intent.getStringExtra("EXTRA_KATEGORI") ?: "Daftar Materi"

        // 2. Hubungkan komponen UI
        tvJudul = findViewById(R.id.tvJudulKategori)
        rv = findViewById(R.id.rvDaftarMateri)

        // Ubah teks judul di atas layar sesuai kategori yang diklik
        tvJudul.text = kategoriTerpilih

        // Atur RecyclerView agar bentuknya memanjang ke bawah (List)
        rv.layoutManager = LinearLayoutManager(this)

        // 3. Siapkan koneksi API
        sessionManager = SessionManager(this)
        api = ApiClient.create(sessionManager, baseUrl = "https://fansnime.my.id/")

        // 4. Mulai ambil dan saring data
        loadMateri()
    }

    private fun loadMateri() {
        lifecycleScope.launch {
            try {
                // Ambil SEMUA materi dari API
                val res = api.getMaterials()

                // PROSES FILTERING: Saring data, ambil yang nama kategorinya SAMA persis
                val materiTersaring = res.data.filter { materi ->
                    materi.category.equals(kategoriTerpilih, ignoreCase = true)
                }

                // Masukkan data yang sudah disaring ke Adapter
                adapter = DaftarMateriAdapter(materiTersaring) { materiDiklik ->

                    // Kalau salah satu materi diklik, buka halaman Detail Materi!
                    val intent = Intent(this@DaftarMateriActivity, MateriDetailActivity::class.java)
                    intent.putExtra("material_id", materiDiklik.id) // Kirim ID materi ke halaman detail
                    startActivity(intent)

                }

                rv.adapter = adapter

                // Jika ternyata kategorinya kosong (belum ada materi dari guru)
                if (materiTersaring.isEmpty()) {
                    Toast.makeText(this@DaftarMateriActivity, "Belum ada materi di kategori ini.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@DaftarMateriActivity, "Error memuat data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}