package com.khoavna.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(
    val type: String,
    val id: String
) : Parcelable