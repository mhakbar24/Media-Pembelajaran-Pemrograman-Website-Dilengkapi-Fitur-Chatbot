package com.activity.chatbot
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.activity.chatbot.api.MaterialItem

class MateriAdapter(
    private var items: List<MaterialItem>,
    private val onClick: (MaterialItem) -> Unit
) : RecyclerView.Adapter<MateriAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvMeta: TextView = v.findViewById(R.id.tvMeta)
        val iconType: ImageView = v.findViewById(R.id.iconType)
        val imgDone: ImageView = v.findViewById(R.id.imgDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title

        // Subjudul: pakai category + created_at (kamu bisa ganti nanti)
        val cat = item.category.ifBlank { "-" }
        val date = item.created_at ?: "-"
        holder.tvMeta.text = "$cat • $date"

        // icon & done sementara statis dulu
        holder.iconType.setImageResource(R.drawable.ic_play) // pastikan drawable ada
        holder.imgDone.visibility = View.GONE // kalau belum ada status selesai

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size

    fun submit(newItems: List<MaterialItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
