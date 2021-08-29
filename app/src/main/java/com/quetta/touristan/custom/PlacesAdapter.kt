package com.quetta.touristan.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quetta.touristan.R
import com.quetta.touristan.gone
import com.quetta.touristan.model.PlaceItem
import com.quetta.touristan.viewmodel.HomeViewModel
import com.quetta.touristan.visible


class PlacesAdapter(
    private val context: Context,
    private val vmHome: HomeViewModel,
    private var places: List<PlaceItem>? = null,
    private val onClick: ((PlaceItem) -> Unit)? = null
) :
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    inner class ViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.fragment_suggested_places_list_dialog_item,
            parent,
            false
        )
    ) {

        val title: TextView = itemView.findViewById(R.id.title)
        val primary: TextView = itemView.findViewById(R.id.textPrimary)
        val secondary: TextView = itemView.findViewById(R.id.textSecondary)
        val image: ImageView = itemView.findViewById(R.id.img)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        places?.get(position)?.let { item ->
            holder.title.text = item.name
            holder.primary.text = item.address
            holder.ratingBar.rating = item.reviews

            val img = item.getImageUrl()

            if (img != null) {
                Glide.with(context)
                    .load(img)
//                    .apply(RequestOptions().override(800, 800))
                    .centerCrop()
                    .error(R.drawable.ic_img_error)
                    .placeholder(R.drawable.ic_image)
                    .into(holder.image)
                holder.image.visible()
            } else {
                holder.image.setImageResource(0)
                holder.image.gone()
            }

            holder.itemView.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    fun updateItems(places: List<PlaceItem>) {
        this.places = places
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }
}