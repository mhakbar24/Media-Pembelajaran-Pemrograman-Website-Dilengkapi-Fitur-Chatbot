package com.activity.chatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MapGridAdapter(
    private var items: List<MapItem>,
    private val onClick: (MapItem) -> Unit
) : RecyclerView.Adapter<MapGridAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val imgIcon: ImageView = v.findViewById(R.id.imgIcon)
        val tvName: TextView = v.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_map_card, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvName.text = item.title
        holder.imgIcon.setImageResource(item.iconRes)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size

    fun submit(newItems: List<MapItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}