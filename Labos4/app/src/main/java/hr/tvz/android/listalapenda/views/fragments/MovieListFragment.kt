package hr.tvz.android.listalapenda.views.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.listalapenda.views.adapters.MovieAdapter
import hr.tvz.android.listalapenda.app.App
import hr.tvz.android.listalapenda.controllers.MovieController
import hr.tvz.android.listalapenda.controllers.MovieListView
import hr.tvz.android.listalapenda.databinding.FragmentMovieListBinding
import hr.tvz.android.listalapenda.models.Movie
import hr.tvz.android.listalapenda.views.activities.DetailActivity
import hr.tvz.android.listalapenda.views.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListFragment : Fragment(), MovieListView {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var controller: MovieController
    private val TAG = "MovieAppDebug"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        Log.d(TAG, "MovieListFragment onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        controller = MovieController(this, (requireActivity().application as App).movieApi)
        controller.loadMovies()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(mutableListOf()) { movie ->
            Log.d(TAG, "Movie clicked: ${movie.title}")
            val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            if (isLandscape) {
                Log.d(TAG, "Landscape mode, displaying details in fragment")
                (activity as? MainActivity)?.displayMovieDetails(movie)
            } else {
                Log.d(TAG, "Portrait mode, starting DetailActivity")
                if (movie != null) {
                    try {
                        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                            putExtra("movie", movie)
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting DetailActivity: ${e.message}", e)
                    }
                } else {
                    Log.e(TAG, "Movie object is null")
                }
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = movieAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun displayMovies(movies: List<Movie>) {
        movieAdapter.updateMovies(movies)
    }

    override fun showError(message: String) {
        Log.e(TAG, message)
    }
}