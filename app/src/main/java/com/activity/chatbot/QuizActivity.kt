package com.activity.chatbot

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.QuizQuestionItem
import com.activity.chatbot.api.QuizSubmitRequest
import com.activity.chatbot.databinding.ActivityQuizBinding
import com.activity.chatbot.databinding.ScoreDialogBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    private lateinit var binding: ActivityQuizBinding
    private val api by lazy { ApiClient.apiService(this) }
    private val session by lazy { SessionManager(this) }

    private var currentQuestionIndex = 0
    private var selectedAnswer = ""
    private var selectedAnswerKey = ""
    private var score = 0
    private var quizId = -1
    private var isFinishingQuiz = false
    private var timer: CountDownTimer? = null

    private val submittedAnswers = linkedMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizId = intent.getIntExtra("quiz_id", -1)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }

        if (isApiMode()) {
            loadQuestionsFromApi()
        } else {
            if (questionModelList.isEmpty()) {
                Toast.makeText(this, "Tidak ada soal quiz", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            loadQuestions()
            startTimer()
        }
    }

    private fun isApiMode(): Boolean = quizId > 0

    private fun loadQuestionsFromApi() {
        setLoadingState(true)
        lifecycleScope.launch {
            try {
                val response = api.getQuizQuestions(quizId)
                questionModelList = response.questions.map { it.toQuestionModel() }
                if (questionModelList.isEmpty()) {
                    Toast.makeText(this@QuizActivity, "Soal quiz belum tersedia", Toast.LENGTH_SHORT).show()
                    finish()
                    return@launch
                }
                loadQuestions()
                startTimer()
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    handleUnauthorized()
                    return@launch
                }
                if (questionModelList.isNotEmpty()) {
                    loadQuestions()
                    startTimer()
                } else {
                    Toast.makeText(this@QuizActivity, "Gagal memuat soal quiz", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } finally {
                setLoadingState(false)
            }
        }
    }

    private fun QuizQuestionItem.toQuestionModel(): QuestionModel {
        val sorted = options.toSortedMap()
        val baseKeys = sorted.keys.toList().take(4).toMutableList()
        while (baseKeys.size < 4) {
            baseKeys.add((baseKeys.size + 65).toChar().toString())
        }
        val optionValues = baseKeys.map { sorted[it] ?: "-" }
        return QuestionModel(
            question = questionText,
            options = optionValues,
            correct = "",
            questionId = id.toString(),
            optionKeys = baseKeys
        )
    }

    private fun startTimer() {
        val totalMinutes = time.toIntOrNull() ?: 10
        val totalTimeInMillis = totalMinutes * 60 * 1000L
        timer?.cancel()
        timer = object : CountDownTimer(totalTimeInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    private fun loadQuestions() {
        selectedAnswer = ""
        selectedAnswerKey = ""

        if (questionModelList.isEmpty()) {
            finishQuiz()
            return
        }

        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        val current = questionModelList[currentQuestionIndex]
        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex + 1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = current.question
            btn0.text = current.options.getOrNull(0) ?: "-"
            btn1.text = current.options.getOrNull(1) ?: "-"
            btn2.text = current.options.getOrNull(2) ?: "-"
            btn3.text = current.options.getOrNull(3) ?: "-"
        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }

        if (view?.id == R.id.next_btn) {
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(applicationContext, "Silahkan pilih jawaban untuk melanjutkan", Toast.LENGTH_SHORT).show()
                return
            }

            if (isApiMode()) {
                val current = questionModelList[currentQuestionIndex]
                val qId = current.questionId
                if (!qId.isNullOrEmpty()) {
                    submittedAnswers[qId] = selectedAnswerKey
                }
            } else if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
                score++
            }

            currentQuestionIndex++
            loadQuestions()
            return
        }

        val clickedBtn = view as? Button ?: return
        val optionIndex = when (clickedBtn.id) {
            R.id.btn0 -> 0
            R.id.btn1 -> 1
            R.id.btn2 -> 2
            R.id.btn3 -> 3
            else -> -1
        }

        selectedAnswer = clickedBtn.text.toString()

        val keys = questionModelList[currentQuestionIndex].optionKeys
        selectedAnswerKey = when {
            optionIndex >= 0 && !keys.isNullOrEmpty() -> keys.getOrNull(optionIndex) ?: selectedAnswer
            else -> selectedAnswer
        }

        clickedBtn.setBackgroundColor(getColor(R.color.grean))
    }

    private fun finishQuiz() {
        if (isFinishingQuiz) return
        isFinishingQuiz = true

        if (isApiMode()) {
            submitQuizAndShowScore()
            return
        }

        showScoreDialog(score, questionModelList.size)
    }

    private fun submitQuizAndShowScore() {
        setLoadingState(true)
        lifecycleScope.launch {
            try {
                val response = api.submitQuizAnswers(
                    quizId,
                    QuizSubmitRequest(
                        quizId = quizId,
                        answers = submittedAnswers
                    )
                )
                val serverScore = runCatching {
                    api.getQuizResult(quizId).data?.score
                }.getOrNull() ?: response.score
                showScoreDialog(serverScore, response.total)
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    handleUnauthorized()
                    return@launch
                }
                Toast.makeText(this@QuizActivity, "Gagal submit quiz", Toast.LENGTH_SHORT).show()
                showScoreDialog(score, questionModelList.size)
            } finally {
                setLoadingState(false)
            }
        }
    }

    private fun handleUnauthorized() {
        session.logout()
        Toast.makeText(this, "Sesi berakhir, silakan login lagi.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginFormActivity::class.java))
        finishAffinity()
    }

    override fun onDestroy() {
        timer?.cancel()
        timer = null
        super.onDestroy()
    }

    private fun showScoreDialog(correctCount: Int, totalQuestions: Int) {
        val safeTotal = if (totalQuestions <= 0) 1 else totalQuestions
        val percentage = ((correctCount.toFloat() / safeTotal.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Selamat kamu berhasil menjawab semuanya!!!"
                scoreTitle.setTextColor(Color.GREEN)
            } else {
                scoreTitle.text = "Maaf, Masih ada yang salah coba lagi yuk!!!"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$correctCount benar dari $totalQuestions soal"
            finishBtn.setOnClickListener { finish() }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }

    private fun setLoadingState(loading: Boolean) {
        binding.nextBtn.isEnabled = !loading
        binding.btn0.isEnabled = !loading
        binding.btn1.isEnabled = !loading
        binding.btn2.isEnabled = !loading
        binding.btn3.isEnabled = !loading
    }
}