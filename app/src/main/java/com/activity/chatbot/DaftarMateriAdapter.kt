package com.activity.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.activity.chatbot.api.MaterialItem
class DaftarMateriAdapter(
    private val listMateri: List<MaterialItem>,
    private val onItemClick: (MaterialItem) -> Unit
) : RecyclerView.Adapter<DaftarMateriAdapter.MateriViewHolder>() {

    // 1. Menyambungkan komponen UI dari item_daftar_materi.xml
    class MateriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIkon = itemView.findViewById<ImageView>(R.id.imgIkonList)
        val tvJudul = itemView.findViewById<TextView>(R.id.tvJudulMateriList)
        val tvGuru = itemView.findViewById<TextView>(R.id.tvGuruMateriList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        // Mengembangkan (inflate) desain item_daftar_materi.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar_materi, parent, false)
        return MateriViewHolder(view)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val materi = listMateri[position]

        // 2. Memasang data dari API ke layar
        holder.tvJudul.text = materi.title

        // Karena data 'teacher' dari API-mu bisa null, kita beri perlindungan
        val namaGuru = materi.teacher?.name ?: "Guru Tidak Diketahui"
        holder.tvGuru.text = "Oleh: $namaGuru"

        // Gambar ikon default sementara
        holder.imgIkon.setImageResource(R.mipmap.ic_launcher)

        // 3. Membuat baris bisa diklik untuk pindah ke halaman Detail
        holder.itemView.setOnClickListener {
            onItemClick(materi)
        }
    }

    override fun getItemCount(): Int {
        return listMateri.size
    }
}