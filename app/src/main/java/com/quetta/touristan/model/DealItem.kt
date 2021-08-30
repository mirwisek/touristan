package com.quetta.touristan.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class DealItem(
    val packageTitle: String?,
    val price: String?,
    val listingItems: ArrayList<ListingItemText>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(ListingItemText)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(packageTitle)
        parcel.writeString(price)
        parcel.writeTypedList(listingItems)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DealItem> {
        override fun createFromParcel(parcel: Parcel): DealItem {
            return DealItem(parcel)
        }

        override fun newArray(size: Int): Array<DealItem?> {
            return arrayOfNulls(size)
        }
    }
}