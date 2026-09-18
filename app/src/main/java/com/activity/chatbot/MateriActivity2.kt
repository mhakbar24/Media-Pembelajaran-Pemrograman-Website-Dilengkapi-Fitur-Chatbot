package com.activity.chatbot
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.ApiService
import com.activity.chatbot.api.MaterialItem
import kotlinx.coroutines.launch
import android.content.Intent

class MateriActivity2 : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: KategoriAdapter
    private lateinit var api: ApiService

    // Variabel untuk menyimpan semua data asli dari API
    private var semuaMateriDariApi: List<MaterialItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi2)

        api = ApiClient.apiService(this)

        // 2. Setup RecyclerView (Grid 2 Kolom)
        rv = findViewById(R.id.rvMap)
        rv.layoutManager = GridLayoutManager(this, 2)

        // 3. Mulai ambil data dari API
        loadFromApi()
    }

    private fun loadFromApi() {
        lifecycleScope.launch {
            try {
                // Mengambil response dari API
                val res = api.getMaterials()
                semuaMateriDariApi = res.data

                // --- PROSES MENYARING KATEGORI ---
                // Ambil semua tulisan kategori, lalu hapus yang duplikat
                val daftarKategoriUnik = semuaMateriDariApi
                    .map { it.category }
                    .distinct()

                // Pasangkan teks kategori dengan gambar ikon
                val listKategoriUI = daftarKategoriUnik.map { namaKategori ->

                    // Nanti kamu bisa ubah R.mipmap.ic_launcher dengan gambar aslimu (misal R.drawable.ic_hardware)
                    val ikonPilihan = when {
                        namaKategori.contains("Hardware", ignoreCase = true) -> R.drawable.hi_bot
                        namaKategori.contains("Software", ignoreCase = true) -> R.mipmap.ic_launcher
                        else -> R.drawable.hi_bot // Gambar default
                    }

                    // Masukkan ke dalam Data Class
                    KategoriUI(
                        namaKategori = namaKategori,
                        gambarKategori = ikonPilihan
                    )
                }

                // --- PROSES MENAMPILKAN KE LAYAR ---
                // Masukkan data yang sudah rapi ke adapter
                adapter = KategoriAdapter(listKategoriUI) { kategoriTerpilih ->


                    val intent = Intent(this@MateriActivity2, DaftarMateriActivity::class.java)

                    // Bawa nama kategori sebagai "tiket" ke halaman selanjutnya
                    intent.putExtra("EXTRA_KATEGORI", kategoriTerpilih.namaKategori)

                    startActivity(intent)

                }

                rv.adapter = adapter

            } catch (e: Exception) {
                // Munculkan error jika API gagal dipanggil (misal tidak ada internet)
                Toast.makeText(
                    this@MateriActivity2,
                    "Gagal memuat kategori: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}