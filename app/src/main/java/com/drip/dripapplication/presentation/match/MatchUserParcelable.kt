package com.drip.dripapplication.presentation.match

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MatchUserParcelable(
    val photo: String,
    val name: String
): Parcelable
