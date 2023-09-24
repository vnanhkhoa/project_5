package com.khoavna.politicalpreparedness.representative.model

import android.os.Parcelable
import com.khoavna.politicalpreparedness.network.models.Office
import com.khoavna.politicalpreparedness.network.models.Official
import kotlinx.parcelize.Parcelize

@Parcelize
data class Representative(
    val official: Official,
    val office: Office
) : Parcelable