package hr.tvz.android.listalapenda.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val title: String,
    val director: String,
    val year: Int,
    val description: String,
    val imageResId: Int,
    val url: String
) : Parcelable