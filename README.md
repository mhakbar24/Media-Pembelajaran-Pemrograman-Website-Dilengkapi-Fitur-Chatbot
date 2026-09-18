# 📚 Media Pembelajaran Pemrograman Website Dilengkapi Fitur Chatbot

Aplikasi Android media pembelajaran interaktif untuk siswa **SMK** yang memadukan materi belajar, kuis evaluasi, dan **chatbot AI** sebagai asisten belajar. Aplikasi ini terhubung ke backend REST API untuk manajemen akun siswa/guru, materi, dan progres belajar.

## ✨ Fitur Utama

- **Autentikasi Siswa & Guru** — login, registrasi (dengan foto profil), dan manajemen sesi menggunakan token.
- **Chatbot AI** — tanya jawab interaktif seputar materi pelajaran, didukung Google Generative AI dengan model **`gemini-3-flash-preview`**.
- **Materi Pembelajaran** — daftar materi, detail materi, peta materi, serta pelacakan akses materi.
- **Kuis & Evaluasi** — pengerjaan kuis, pengiriman jawaban, dan riwayat hasil kuis.
- **Progress Overview & AI Insight** — ringkasan progres belajar siswa beserta analisis progres berbasis AI (riwayat & detail).
- **Modul Perawatan Komputer** — materi pembersihan, perawatan hardware/software, prosedur, penggunaan, dan troubleshooting perangkat.
- **Studi Kasus & Kompetensi** — modul studi kasus dan kompetensi sebagai pelengkap pembelajaran.
- **Profil Pengguna** — lihat dan perbarui data profil siswa (termasuk foto profil).

## 🛠️ Tech Stack

| Kategori | Teknologi |
|---|---|
| Bahasa | Kotlin (+ sebagian Java) |
| Arsitektur | MVVM (ViewModel, LiveData) |
| Networking | Retrofit2, OkHttp3 (+ Logging Interceptor) |
| AI | Google Generative AI SDK — model `gemini-3-flash-preview` |
| UI | Material Components, ViewBinding, Splash Screen API |
| Media & Gambar | Glide, Media3 ExoPlayer, CircleImageView |
| Build Tool | Gradle (Kotlin DSL) |
| Min SDK / Target SDK | 24 / 33 |

## 🏗️ Arsitektur & Struktur Proyek

```
app/src/main/java/com/activity/chatbot/
├── api/                 # Retrofit service, model request/response, interceptor
├── ChatbotActivity.kt   # Layar chatbot AI
├── ChatViewModel.kt     # ViewModel untuk logika chat
├── MateriActivity*.kt   # Layar materi & detail materi
├── QuizActivity.kt      # Kuis & evaluasi
├── ProfileActivity.kt   # Profil siswa
├── LoginActivity.kt     # Autentikasi
└── ...                  # Modul perawatan, prosedur, troubleshooting, dsb.
```

Aplikasi berkomunikasi dengan backend REST API (Laravel) yang menyediakan endpoint autentikasi, materi, kuis, chat, dan progres siswa.

## 🚀 Instalasi & Menjalankan Proyek

### Prasyarat
- Android Studio (versi terbaru direkomendasikan)
- JDK 8+
- Perangkat/emulator dengan Android 7.0 (API 24) ke atas

### Langkah

1. **Clone repository**
   ```bash
   git clone https://github.com/mhakbar24/Media-Pembelajaran-Pemrograman-Website-Dilengkapi-Fitur-Chatbot.git
   ```
2. **Buka di Android Studio**
   - Pilih `Open`, arahkan ke folder hasil clone.
3. **Sinkronkan Gradle**
   - Tunggu proses Gradle sync selesai secara otomatis.
4. **Konfigurasi API Key (jika diperlukan)**
   - Tambahkan API key Generative AI (Gemini) sesuai konfigurasi proyek (`local.properties` / `BuildConfig`).
5. **Jalankan aplikasi**
   - Pilih emulator atau perangkat fisik, lalu klik `Run ▶`.

## 🔌 Integrasi Backend

Aplikasi ini dirancang untuk berkomunikasi dengan API backend (contoh endpoint: `teacher/login`, `student/login`, `student/register`, `chat`, `materi`, `quiz`, `student/progress-overview`, dll). Pastikan base URL backend telah dikonfigurasi dengan benar sebelum menjalankan aplikasi.

### `ApiClient` — Objek Konfigurasi Retrofit

Semua komunikasi ke backend REST API melewati satu objek singleton `ApiClient` (`app/src/main/java/com/activity/chatbot/api/ApiClient.kt`), dengan dua cara pemakaian:

1. **`ApiClient.apiService(context)`** — cara default yang dipakai di hampir semua Activity/ViewModel.
   - Base URL sudah *hardcoded* ke `https://fansnime.my.id/api/`.
   - Membangun `OkHttpClient` yang otomatis menyisipkan `AuthInterceptor` (menambahkan token sesi dari `SessionManager` ke setiap request).
   - Logging body request/response (`HttpLoggingInterceptor`) hanya aktif saat `BuildConfig.DEBUG = true`, jadi tidak muncul di build release.
   - Response di-parse dengan `GsonConverterFactory`.

2. **`ApiClient.create(sessionManager, baseUrl)`** — versi fleksibel yang menerima base URL kustom (misalnya untuk mengarahkan aplikasi ke server lain saat development/testing).
   - `baseUrl` dinormalisasi otomatis lewat `normalizeBaseUrl()`: menghapus trailing slash, lalu menambahkan `/api/` di akhir jika belum ada.
   - Tetap menyisipkan `AuthInterceptor` yang sama, tapi **tanpa** logging interceptor.

Kedua metode menghasilkan implementasi `ApiService` (interface Retrofit yang mendefinisikan seluruh endpoint — login, register, chat, materi, kuis, progress, dsb.) yang siap dipanggil secara `suspend`/coroutine.

> ⚠️ Karena base URL default di-hardcode ke domain milik pengembang, untuk menjalankan aplikasi melawan backend sendiri, ubah nilai `BASE_URL` di `ApiClient.kt` atau gunakan overload `create(sessionManager, baseUrl)` sesuai kebutuhan.

## 🤝 Kontribusi

Kontribusi berupa laporan bug, saran fitur, maupun pull request sangat terbuka. Silakan buat *issue* terlebih dahulu untuk mendiskusikan perubahan besar.

## 📄 Lisensi

Proyek ini belum menetapkan lisensi. Tambahkan berkas `LICENSE` sesuai kebutuhan (misalnya MIT) jika ingin membuka proyek secara resmi.

## 👤 Pengembang

Dikembangkan oleh [mhakbar24](https://github.com/mhakbar24) sebagai bagian dari media pembelajaran berbasis AI untuk siswa SMK.

---

# 📚 Programming Learning Media Website with Chatbot Feature

An interactive Android learning app for **vocational high school (SMK)** students that combines learning materials, evaluation quizzes, and an **AI chatbot** as a learning assistant. The app connects to a REST API backend for student/teacher account management, materials, and learning progress.

## ✨ Key Features

- **Student & Teacher Authentication** — login, registration (with profile photo), and token-based session management.
- **AI Chatbot** — interactive Q&A about course material, powered by Google Generative AI with the **`gemini-3-flash-preview`** model.
- **Learning Materials** — material list, material detail, material map, and material access tracking.
- **Quizzes & Evaluation** — taking quizzes, submitting answers, and viewing quiz result history.
- **Progress Overview & AI Insight** — a summary of the student's learning progress plus AI-based progress analysis (history & detail).
- **Computer Maintenance Modules** — cleaning, hardware/software maintenance, procedures, usage guides, and device troubleshooting.
- **Case Studies & Competency** — case study and competency modules as supplementary learning content.
- **User Profile** — view and update student profile data (including profile photo).

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin (+ some Java) |
| Architecture | MVVM (ViewModel, LiveData) |
| Networking | Retrofit2, OkHttp3 (+ Logging Interceptor) |
| AI | Google Generative AI SDK — `gemini-3-flash-preview` model |
| UI | Material Components, ViewBinding, Splash Screen API |
| Media & Images | Glide, Media3 ExoPlayer, CircleImageView |
| Build Tool | Gradle (Kotlin DSL) |
| Min SDK / Target SDK | 24 / 33 |

## 🏗️ Architecture & Project Structure

```
app/src/main/java/com/activity/chatbot/
├── api/                 # Retrofit service, request/response models, interceptor
├── ChatbotActivity.kt   # AI chatbot screen
├── ChatViewModel.kt     # ViewModel for chat logic
├── MateriActivity*.kt   # Material list & detail screens
├── QuizActivity.kt      # Quiz & evaluation
├── ProfileActivity.kt   # Student profile
├── LoginActivity.kt     # Authentication
└── ...                  # Maintenance, procedure, troubleshooting modules, etc.
```

The app communicates with a REST API backend (Laravel) that provides endpoints for authentication, materials, quizzes, chat, and student progress.

## 🚀 Installation & Running the Project

### Prerequisites
- Android Studio (latest version recommended)
- JDK 8+
- A device/emulator running Android 7.0 (API 24) or higher

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/mhakbar24/Media-Pembelajaran-Pemrograman-Website-Dilengkapi-Fitur-Chatbot.git
   ```
2. **Open in Android Studio**
   - Select `Open`, then point it to the cloned folder.
3. **Sync Gradle**
   - Wait for the Gradle sync process to finish automatically.
4. **Configure the API Key (if needed)**
   - Add your Generative AI (Gemini) API key according to the project configuration (`local.properties` / `BuildConfig`).
5. **Run the app**
   - Select an emulator or physical device, then click `Run ▶`.

## 🔌 Backend Integration

This app is designed to communicate with a backend API (example endpoints: `teacher/login`, `student/login`, `student/register`, `chat`, `materi`, `quiz`, `student/progress-overview`, etc.). Make sure the backend base URL is configured correctly before running the app.

### `ApiClient` — Retrofit Configuration Object

All communication with the backend REST API goes through a single singleton object, `ApiClient` (`app/src/main/java/com/activity/chatbot/api/ApiClient.kt`), used in two ways:

1. **`ApiClient.apiService(context)`** — the default method used in almost every Activity/ViewModel.
   - The base URL is hardcoded to `https://fansnime.my.id/api/`.
   - Builds an `OkHttpClient` that automatically attaches an `AuthInterceptor` (adding the session token from `SessionManager` to every request).
   - Request/response body logging (`HttpLoggingInterceptor`) is only enabled when `BuildConfig.DEBUG = true`, so it never appears in release builds.
   - Responses are parsed with `GsonConverterFactory`.

2. **`ApiClient.create(sessionManager, baseUrl)`** — a flexible variant that accepts a custom base URL (e.g. to point the app at a different server during development/testing).
   - The `baseUrl` is automatically normalized via `normalizeBaseUrl()`: it strips any trailing slash, then appends `/api/` at the end if not already present.
   - Still attaches the same `AuthInterceptor`, but **without** the logging interceptor.

Both methods produce an implementation of `ApiService` (the Retrofit interface defining every endpoint — login, register, chat, materials, quizzes, progress, etc.) ready to be called as `suspend`/coroutine functions.

> ⚠️ Because the default base URL is hardcoded to the developer's own domain, to run the app against your own backend, change the `BASE_URL` value in `ApiClient.kt` or use the `create(sessionManager, baseUrl)` overload as needed.

## 🤝 Contributing

Bug reports, feature suggestions, and pull requests are welcome. Please open an issue first to discuss any major changes.

## 📄 License

This project has not yet defined a license. Add a `LICENSE` file as needed (e.g. MIT) if you want to open-source the project officially.

## 👤 Developer

Developed by [mhakbar24](https://github.com/mhakbar24) as part of an AI-powered learning media project for vocational high school (SMK) students.
