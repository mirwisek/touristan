package com.quetta.touristan.custom

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.quetta.touristan.R
import com.quetta.touristan.model.CategoryItem
import com.quetta.touristan.model.PlaceType

class CategoriesAdapter(
    private val context: Context,
    private var places: List<CategoryItem>? = null,
    private var onClick: ((item: String) -> Unit)? = null
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.item_category,
            parent,
            false
        )
    ) {

        val icon: ImageView = itemView.findViewById(R.id.img)
        val label: TextView = itemView.findViewById(R.id.label)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        places?.get(position)?.let { item ->
            val color = ContextCompat.getColor(context, PlaceType.colors[position])
            ContextCompat.getDrawable(context, item.color)?.let { unwrapped ->
                val wrapped = DrawableCompat.wrap(unwrapped)
                DrawableCompat.setTint(wrapped, color)
                holder.icon.setImageDrawable(wrapped)
            }
            holder.label.text = item.name
            holder.label.setTextColor(color)
            if(item.isSelected)
                holder.label.setTypeface(null, Typeface.BOLD)
            else
                holder.label.setTypeface(null, Typeface.NORMAL)

            holder.itemView.setOnClickListener {
                places?.forEach { i ->
                    i.isSelected = false
                }
                item.isSelected = true
                notifyDataSetChanged()
                onClick?.invoke(item.name)
            }
        }
    }

    fun updateItems(places: List<CategoryItem>) {
        this.places = places
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }
}