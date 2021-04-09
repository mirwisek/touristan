package com.quetta.touristan.model

import com.quetta.touristan.R

object PlaceType {
    const val HOSPITAL = "Hospitals"
    const val PICNIC = "Picnic Spots"
    const val BANK = "Banks"
    const val RESTAURANT = "Restaurants"
    const val UNIVERSITY = "Universities"

    val allValues = listOf(
        CategoryItem(HOSPITAL, R.drawable.ic_hospital),
        CategoryItem(PICNIC, R.drawable.ic_tour),
        CategoryItem(BANK, R.drawable.ic_bank),
        CategoryItem(RESTAURANT, R.drawable.ic_restaurant),
        CategoryItem(UNIVERSITY, R.drawable.ic_university)
    )
    val colors = listOf(
        R.color.category1,
        R.color.category2,
        R.color.category3,
        R.color.category4,
        R.color.category5
    )

    fun getApiValue(name: String): String {
        return when (name) {
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