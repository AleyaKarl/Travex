package com.example.travex

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    val placeName: String = "",
    val placeImage: String = "",
    val placeDetails: String = ""
) : Parcelable
