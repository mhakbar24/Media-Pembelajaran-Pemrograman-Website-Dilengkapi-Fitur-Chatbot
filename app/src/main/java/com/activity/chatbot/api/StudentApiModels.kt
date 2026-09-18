package com.activity.chatbot.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val message: String
)

data class ChatResponse(
    val reply: String? = null,
    val error: String? = null
)

data class UpdateStudentResponse(
    val message: String,
    val student: UserDto
)

data class MaterialTrackResponse(
    val ok: Boolean,
    val message: String,
    val data: MaterialTrackData? = null
)

data class MaterialTrackData(
    val id: Int,
    @SerializedName("student_id")
    val studentId: Int,
    @SerializedName("materi_id")
    val materiId: Int,
    @SerializedName("accessed_at")
    val accessedAt: String
)

data class QuizItem(
    val id: Int,
    val title: String,
    val description: String? = null,
    @SerializedName("teacher_id")
    val teacherId: Int? = null,
    val questions: List<QuizQuestionSummary> = emptyList()
)

data class QuizQuestionSummary(
    val id: Int,
    @SerializedName("quiz_id")
    val quizId: Int,
    @SerializedName("question_text")
    val questionText: String
)

data class QuizQuestionsResponse(
    val quiz: String,
    val questions: List<QuizQuestionItem>
)

data class QuizQuestionItem(
    val id: Int,
    @SerializedName("question_text")
    val questionText: String,
    val options: Map<String, String> = emptyMap()
)

data class QuizSubmitRequest(
    @SerializedName("quiz_id")
    val quizId: Int,
    val answers: Map<String, String>
)

data class QuizSubmitResponse(
    val message: String,
    val score: Int,
    val total: Int,
    val result: QuizResultData
)

data class QuizResultResponse(
    val ok: Boolean,
    val message: String? = null,
    val data: StudentQuizResultItem? = null
)

data class StudentQuizResultsResponse(
    val results: List<StudentQuizResultItem> = emptyList()
)

data class StudentQuizResultItem(
    val id: Int,
    @SerializedName("student_id")
    val studentId: Int,
    @SerializedName("quiz_id")
    val quizId: Int,
    val score: Int,
    val quiz: QuizShort? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)

data class QuizResultData(
    val id: Int,
    @SerializedName("student_id")
    val studentId: Int,
    @SerializedName("quiz_id")
    val quizId: Int,
    val score: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val quiz: QuizShort? = null
)

data class QuizShort(
    val id: Int,
    val title: String
)

data class ProgressOverviewResponse(
    val ok: Boolean,
    val student: StudentMini,
    @SerializedName("quiz_progress")
    val quizProgress: QuizProgressOverview,
    @SerializedName("materi_progress")
    val materiProgress: MateriProgressOverview
)

data class StudentMini(
    val id: Int,
    val name: String,
    val email: String
)

data class QuizProgressOverview(
    @SerializedName("total_attempts")
    val totalAttempts: Int,
    @SerializedName("average_score")
    val averageScore: Double,
    @SerializedName("best_score")
    val bestScore: Int,
    @SerializedName("latest_score")
    val latestScore: Int,
    val trend: String
)

data class MateriProgressOverview(
    @SerializedName("total_materi_available")
    val totalMateriAvailable: Int,
    @SerializedName("unique_materi_accessed")
    val uniqueMateriAccessed: Int,
    @SerializedName("coverage_percent")
    val coveragePercent: Double,
    @SerializedName("recent_accesses")
    val recentAccesses: List<MaterialTrackData> = emptyList()
)

data class ProgressAiRequest(
    val context: String? = null
)

data class ProgressAiResponse(
    val ok: Boolean,
    val message: String? = null,
    val student: StudentMini? = null,
    val stats: ProgressAiStats? = null,
    val analysis: String? = null,
    @SerializedName("analysis_sections")
    val analysisSections: AnalysisSections? = null,
    @SerializedName("quality_badge")
    val qualityBadge: QualityBadge? = null,
    @SerializedName("history_id")
    val historyId: Int? = null
)

data class ProgressAiStats(
    @SerializedName("total_quiz")
    val totalQuiz: Int,
    @SerializedName("average_score")
    val averageScore: Double,
    @SerializedName("best_score")
    val bestScore: Int,
    @SerializedName("latest_score")
    val latestScore: Int,
    @SerializedName("materi_coverage_percent")
    val materiCoveragePercent: Double
)

data class AnalysisSections(
    val ringkasan: String? = null,
    val kekuatan: String? = null,
    @SerializedName("perlu_ditingkatkan")
    val perluDitingkatkan: String? = null,
    @SerializedName("tindak_lanjut")
    val tindakLanjut: String? = null
)

data class QualityBadge(
    val key: String,
    val label: String
)

data class ProgressAiHistoryResponse(
    val ok: Boolean,
    val student: StudentMini,
    val count: Int,
    val history: List<ProgressAiHistoryItem> = emptyList()
)

data class ProgressAiHistoryItem(
    val id: Int,
    @SerializedName("total_quiz")
    val totalQuiz: Int,
    @SerializedName("average_score")
    val averageScore: Double,
    val analysis: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class ProgressAiHistoryDetailResponse(
    val ok: Boolean,
    val message: String? = null,
    val data: ProgressAiHistoryDetailData? = null,
    @SerializedName("analysis_sections")
    val analysisSections: AnalysisSections? = null,
    @SerializedName("quality_badge")
    val qualityBadge: QualityBadge? = null
)

data class ProgressAiHistoryDetailData(
    val id: Int,
    @SerializedName("student_id")
    val studentId: Int,
    val analysis: String,
    @SerializedName("created_at")
    val createdAt: String
)

