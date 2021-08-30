package com.quetta.touristan.model

import android.os.Parcel
import android.os.Parcelable
import com.quetta.touristan.R

data class ListingItemText(
    val text: String?,
    val icon: Int = R.drawable.ic_check,
    val color: Int = R.color.check
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeInt(icon)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListingItemText> {
        override fun createFromParcel(parcel: Parcel): ListingItemText {
            return ListingItemText(parcel)
        }

        override fun newArray(size: Int): Array<ListingItemText?> {
            return arrayOfNulls(size)
        }
    }
}
