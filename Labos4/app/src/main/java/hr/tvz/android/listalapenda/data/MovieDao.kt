package hr.tvz.android.listalapenda.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hr.tvz.android.listalapenda.models.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Insert
    suspend fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
}