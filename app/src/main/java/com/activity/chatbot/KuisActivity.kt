package com.activity.chatbot

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.QuizItem
import com.activity.chatbot.databinding.ActivityKuisBinding
import kotlinx.coroutines.launch

class KuisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKuisBinding
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        loadQuizList()
    }

    private fun setupRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadQuizList() {
        lifecycleScope.launch {
            runCatching { ApiClient.apiService(this@KuisActivity).getQuizList() }
                .onSuccess { apiQuiz ->
                    quizModelList.clear()
                    quizModelList.addAll(apiQuiz.mapToUiModel())
                    setupRecyclerView()
                }
                .onFailure {
                    Toast.makeText(
                        this@KuisActivity,
                        "Quiz online gagal dimuat, pakai mode offline.",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadOfflineQuiz()
                }
        }
    }

    private fun List<QuizItem>.mapToUiModel(): List<QuizModel> {
        return map { item ->
            QuizModel(
                id = item.id.toString(),
                title = item.title,
                subtitle = item.description ?: "Quiz siswa",
                time = "10",
                questionList = emptyList()
            )
        }
    }

    private fun loadOfflineQuiz() {
        val listQuestionModel = mutableListOf<QuestionModel>()
        listQuestionModel.add(
            QuestionModel(
                "Debu pada keyboard akan menyebabkan....",
                mutableListOf("Double ketik", "Tekanan terasa berat", "Tidak menjadi acak", "Keyboard akan tidak terbaca", "Keyboard tidak terdeteksi"),
                "Tekanan terasa berat"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Berikut kerusakan yang sering terjadi pada printer,kecuali...",
                mutableListOf("Printer tidak menarik kertas", "Hasil cetakan tidak bagus", "printer tidak mendeteksi perintah komputer", "Tombol macet"),
                "Tombol macet"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Noda atau kotoran kering pada motherboard dapat dibersihkan dengan...",
                mutableListOf("Disk cleaner", "Flash cleaner", "Penyedot debu mini", "Cairan pembersih"),
                "Cairan pembersih"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Yang termasuk alat-alat untuk perawatan PC, kecuali...",
                mutableListOf("Palu", "Vaccum cleaner", "Cd cleaner", "Catton bud", "Kuas"),
                "Palu"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Papan induk yang berfungsi untuk meletakkan semua komponen yang ada pada CPU adalah....",
                mutableListOf("Chasing", "Motherboard", "CPU", "Processor", "Box Fan"),
                "Motherboard"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Debu-debu yang menempael pada sirip-sirip heatsinkfan dan lubang fentilasi cassing dibersihkan menggunakan..",
                mutableListOf("Disk cleaner", "CD cleaner", "Cairan pembersih", "Kuas"),
                "Kuas"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Type perawatan PC ada 2 yaitu...",
                mutableListOf("Perawatan aktif dan pasif", "Perawatan luar dan dalam", "Perawatan pasif dan preventive", "Perawatan secara manual dan otomatis"),
                "Perawatan aktif dan pasif"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Membersihkan PC dari debu dan komponen lain adalah…",
                mutableListOf("Perawatan Bagian Dalam", "Perawatan Bagian Luar", "Perawatan hardware", "Perawatan software"),
                "Perawatan hardware"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Istilah dari perangkat/perlengkapan pada komputer adalah…",
                mutableListOf("Peripheral", "Hardware", "Tools", "PC"),
                "Peripheral"
            )
        )
        listQuestionModel.add(
            QuestionModel(
                "Meliputi langkah-langkah yang biasa kita gunakan untuk melakukan proteksi sistem terhadap lingkungan yang normal. Berikut adalah pengertian dari...",
                mutableListOf("Disk defragmenter", "Scan disk", "Perawatan pasif", "Perawatan aktif"),
                "Perawatan aktif"
            )
        )


        quizModelList.clear()
        quizModelList.add(
            QuizModel(
                "1",
                "Perakitan dan Perawatan Komputer",
                "Hal basic dalam Perakitan dan Perawatan komputer",
                "10",
                listQuestionModel
            )
        )

        setupRecyclerView()
    }
}