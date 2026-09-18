package com.activity.chatbot.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // Legacy endpoint tetap dipertahankan agar flow teacher lama tidak langsung rusak.
    @POST("teacher/login")
    suspend fun loginTeacher(
        @Body req: LoginRequest
    ): LoginResponse

    @POST("student/login")
    suspend fun loginSiswa(@Body req: LoginRequest): LoginResponse

    @POST("student/register")
    suspend fun registerStudent(
        @Body req: RegisterRequest
    ): RegisterResponse

    @Multipart
    @POST("student/register")
    suspend fun registerStudentMultipart(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part profilePhoto: MultipartBody.Part? = null
    ): RegisterResponse

    @POST("chat")
    suspend fun sendChat(@Body body: ChatRequest): ChatResponse

    @GET("student/profile")
    suspend fun getStudentProfile(): StudentProfileResponse

    @PUT("student/update")
    suspend fun updateStudentProfile(@Body body: UpdateStudentRequest): UpdateStudentResponse

    @Multipart
    @PUT("student/update")
    suspend fun updateStudentProfileMultipart(
        @Part("name") name: RequestBody? = null,
        @Part("email") email: RequestBody? = null,
        @Part("password") password: RequestBody? = null,
        @Part("password_confirmation") passwordConfirmation: RequestBody? = null,
        @Part profilePhoto: MultipartBody.Part? = null
    ): UpdateStudentResponse

    @POST("student/logout")
    suspend fun logoutStudent(): MessageResponse

    @GET("materi")
    suspend fun getMaterials(): MaterialsResponse

    @GET("materi/{id}")
    suspend fun getMaterialDetail(@Path("id") id: Int): MaterialDetailResponse

    @POST("materi/{id}/track")
    suspend fun trackMaterialAccess(@Path("id") id: Int): MaterialTrackResponse

    @GET("quiz")
    suspend fun getQuizList(): List<QuizItem>

    @GET("quiz/{quizId}/questions")
    suspend fun getQuizQuestions(@Path("quizId") quizId: Int): QuizQuestionsResponse

    @POST("quiz/{quizId}/submit")
    suspend fun submitQuizAnswers(
        @Path("quizId") quizId: Int,
        @Body body: QuizSubmitRequest
    ): QuizSubmitResponse

    @GET("quiz/{quizId}/result")
    suspend fun getQuizResult(@Path("quizId") quizId: Int): QuizResultResponse

    @GET("student/quiz/results")
    suspend fun getStudentQuizResults(): StudentQuizResultsResponse

    @GET("student/progress-overview")
    suspend fun getProgressOverview(): ProgressOverviewResponse

    @POST("student/progress-ai")
    suspend fun generateProgressAi(@Body body: ProgressAiRequest = ProgressAiRequest()): ProgressAiResponse

    @GET("student/progress-ai/history")
    suspend fun getProgressAiHistory(): ProgressAiHistoryResponse

    @GET("student/progress-ai/history/{id}")
    suspend fun getProgressAiHistoryDetail(@Path("id") id: Int): ProgressAiHistoryDetailResponse
}