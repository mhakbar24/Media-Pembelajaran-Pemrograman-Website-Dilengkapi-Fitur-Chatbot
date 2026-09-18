package com.activity.chatbot

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.activity.chatbot.api.ApiClient
import com.activity.chatbot.api.ApiService
import kotlinx.coroutines.launch

class PetaMateriActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: ModuleTimelineAdapter
    private lateinit var api: ApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peta_materi)

        sessionManager = SessionManager(this)
        api = ApiClient.create(sessionManager, baseUrl = "https://fansnime.my.id/") // baseUrl kamu

        rv = findViewById(R.id.rvModules)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = ModuleTimelineAdapter(emptyList()) { item ->
            val i = Intent(this, MateriDetailActivity::class.java)
            i.putExtra("material_id", item.id)
            startActivity(i)
        }
        rv.adapter = adapter

        load()
    }

    private fun load() {
        lifecycleScope.launch {
            try {
                val res = api.getMaterials()
                adapter.submit(res.data.sortedBy { it.id })
            } catch (e: Exception) {
                Toast.makeText(this@PetaMateriActivity, "Gagal memuat materi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}