package com.activity.chatbot
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Kita menambahkan 'onItemClick' agar nanti kotaknya bisa diklik
class KategoriAdapter(
    private val listKategori: List<KategoriUI>,
    private val onItemClick: (KategoriUI) -> Unit
) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

    // Menyambungkan komponen UI dari item_kategori.xml
    class KategoriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIkon = itemView.findViewById<ImageView>(R.id.imgIkonKategori)
        val tvNama = itemView.findViewById<TextView>(R.id.tvNamaKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        // Mengembangkan (inflate) desain item_kategori.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        // Mengambil data kategori sesuai urutan
        val kategori = listKategori[position]

        // Memasang teks dan gambar ke dalam kotak
        holder.tvNama.text = kategori.namaKategori
        holder.imgIkon.setImageResource(kategori.gambarKategori)

        // Membuat seluruh area kotak bisa diklik
        holder.itemView.setOnClickListener {
            onItemClick(kategori)
        }
    }

    // Menentukan jumlah total kotak yang akan dibuat
    override fun getItemCount(): Int {
        return listKategori.size
    }
}