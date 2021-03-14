package com.quetta.touristan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

object PlaceType {
    const val HOSPITAL = "Hospitals"
    const val PICNIC = "Picnic Spots"
    const val BANK = "Banks"
    const val RESTAURANT = "Restaurants"
    const val UNIVERSITY = "Universities"
    val allValues = listOf(HOSPITAL, PICNIC, BANK, RESTAURANT, UNIVERSITY)

    fun getApiValue(name: String): String {
        return when(name) {
            HOSPITAL -> "hospital"
            BANK -> "bank"
            RESTAURANT -> "restaurant"
            PICNIC -> "tourist_attraction"
            UNIVERSITY -> "university"
            else -> {
                throw IllegalArgumentException("No mapping value found for given argument, this should never occur")
            }
        }
    }
}

class ChipsAdapter(private var places: List<String>? = null, private var onClick: ((item: String) -> Unit)? = null ) :
    RecyclerView.Adapter<ChipsAdapter.ViewHolder>() {

    inner class ViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.item_chips,
            parent,
            false
        )
    ) {

        val chip: Chip = itemView.findViewById(R.id.chips)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        places?.get(position)?.let { item ->
            holder.chip.text = item
            holder.chip.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    fun updateItems(places: List<String>) {
        this.places = places
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }
}