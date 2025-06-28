package hr.tvz.android.listalapenda.models.api

import hr.tvz.android.listalapenda.models.Movie
import retrofit2.http.GET

interface MovieApi {
    @GET("movies")
    suspend fun getAllMovies(): List<Movie>
}