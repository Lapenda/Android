package hr.tvz.android.listalapenda.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.listalapenda.databinding.ActivityImageBinding
import hr.tvz.android.listalapenda.models.Movie

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movie = intent.getParcelableExtra<Movie>("movie")
        if (movie != null) {
            binding.imageView.setImageURI(movie.imageUrl)
        } else {
            binding.imageView.setImageURI("android.resource://android/drawable/ic_menu_gallery")
        }
    }
}