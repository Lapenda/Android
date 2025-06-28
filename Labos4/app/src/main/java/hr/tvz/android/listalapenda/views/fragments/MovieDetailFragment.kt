package hr.tvz.android.listalapenda.views.fragments

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.tvz.android.listalapenda.views.activities.ImageActivity
import hr.tvz.android.listalapenda.R
import hr.tvz.android.listalapenda.databinding.FragmentMovieDetailBinding
import hr.tvz.android.listalapenda.models.Movie

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable("movie")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        (_binding?.root?.parent as? ViewGroup)?.removeView(_binding?.root)
        Log.d("","MovieDetailFragment onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie?.let { displayMovieDetails(it) }
        setupImageClick()
        setupExternalActivity()
    }

    private fun displayMovieDetails(movie: Movie) {
        Log.d("MovieDetailFragment", "ImageUrl: ${movie.imageUrl}")
        binding.titleTextView.text = movie.title
        binding.directorTextView.text = movie.director
        binding.yearTextView.text = movie.year.toString()
        binding.descriptionTextView.text = movie.description
        binding.imageView.setImageURI(movie.imageUrl)
        val fadeIn = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in)
        binding.imageView.startAnimation(fadeIn)
    }

    private fun setupImageClick() {
        binding.imageView.setOnClickListener {
            val intent = Intent(requireContext(), ImageActivity::class.java).apply {
                putExtra("movie", movie)
            }
            startActivity(intent)
        }
    }

    private fun setupExternalActivity() {
        binding.openWebsiteButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie?.url))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}