package com.drip.dripapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchUserParcelable(
    val photo: String,
    val name: String
): Parcelable
