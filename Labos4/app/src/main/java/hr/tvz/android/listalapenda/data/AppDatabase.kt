package hr.tvz.android.listalapenda.data

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.tvz.android.listalapenda.models.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}