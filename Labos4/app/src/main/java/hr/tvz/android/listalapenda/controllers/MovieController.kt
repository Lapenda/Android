package hr.tvz.android.listalapenda.controllers

import hr.tvz.android.listalapenda.models.Movie
import hr.tvz.android.listalapenda.models.api.MovieApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieController(private val view: MovieListView, private val movieApi: MovieApi) {
    fun loadMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val movies = withContext(Dispatchers.IO) {
                    movieApi.getAllMovies()
                }
                view.displayMovies(movies)
            } catch (e: Exception) {
                view.showError("Error loading movies: ${e.message}")
            }
        }
    }
}

interface MovieListView {
    fun displayMovies(movies: List<Movie>)
    fun showError(message: String)
}