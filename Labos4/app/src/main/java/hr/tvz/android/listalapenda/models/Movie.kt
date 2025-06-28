package hr.tvz.android.listalapenda.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val director: String,
    val year: Int,
    val description: String,
    val imageUrl: String,
    val url: String
) : Parcelable