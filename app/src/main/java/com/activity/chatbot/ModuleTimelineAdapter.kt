package com.activity.chatbot

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.activity.chatbot.api.MaterialItem
import com.google.android.material.card.MaterialCardView

class ModuleTimelineAdapter(
    private var items: List<MaterialItem>,
    private val onClick: (MaterialItem) -> Unit
) : RecyclerView.Adapter<ModuleTimelineAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        val tvDesc: TextView = v.findViewById(R.id.tvDesc)
        val tvCategory: TextView = v.findViewById(R.id.tvCategory)
        val iconWrap: MaterialCardView = v.findViewById(R.id.iconWrap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_module_timeline, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        // ---- Bind text ----
        holder.tvTitle.text = item.title
        holder.tvDesc.text = item.description ?: "-"
        holder.tvCategory.text = item.category

        // ---- Color by category (optional) ----
        val cat = item.category.trim().lowercase()
        val colorHex = when {
            cat.contains("html") -> "#F97316"       // orange
            cat.contains("css") -> "#2563EB"        // blue
            cat.contains("javascript") || cat.contains("js") -> "#FACC15" // yellow
            else -> "#7C3AED"                       // purple default
        }
        holder.iconWrap.setCardBackgroundColor(Color.parseColor(colorHex))

        // ---- Smooth fade-in animation ----
        holder.itemView.animate().cancel()
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 14f
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(250)
            .setInterpolator(DecelerateInterpolator())
            .start()

        // ---- Click ----
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun submit(newItems: List<MaterialItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}