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
import com.quetta.touristan.model.ListingItemText
import com.quetta.touristan.model.PlaceType

class DealListingAdapter(
    private val context: Context,
    private var items: List<ListingItemText>? = null
) :
    RecyclerView.Adapter<DealListingAdapter.ViewHolder>() {

    inner class ViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.item_deal_listing,
            parent,
            false
        )
    ) {

        val icon: ImageView = itemView.findViewById(R.id.icon)
        val label: TextView = itemView.findViewById(R.id.text)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        items?.get(position)?.let { item ->
            val color = ContextCompat.getColor(context, item.color)
            ContextCompat.getDrawable(context, item.icon)?.let { unwrapped ->
                val wrapped = DrawableCompat.wrap(unwrapped)
                DrawableCompat.setTint(wrapped, color)
                holder.icon.setImageDrawable(wrapped)
            }
            holder.label.text = item.text
//            holder.label.setTextColor(color)
        }
    }

    fun updateItems(places: List<ListingItemText>) {
        this.items = places
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }
}