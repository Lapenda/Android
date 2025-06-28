package hr.tvz.android.listalapenda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import hr.tvz.android.listalapenda.databinding.ActivityMainBinding
import hr.tvz.android.listalapenda.models.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Build
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val shareReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            sharedPreferences.edit().putBoolean("share_broadcast_sent", true).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        sharedPreferences = getSharedPreferences("SharePrefs", MODE_PRIVATE)

        val isLandscape = resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
        if (!isLandscape) {
            val detailFragment = supportFragmentManager.findFragmentById(R.id.detailFragmentContainer)
            if (detailFragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(detailFragment)
                    .commit()
                Log.d("MainActivity", "Removed detail fragment in portrait mode")
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            (application as App).database.movieDao().clearAllMovies()

            val movies = (application as App).database.movieDao().getAllMovies()
            if (movies.isEmpty()) {
                val sampleMovies = listOf(
                    Movie(0, "The Shawshank Redemption", "Frank Darabont", 1994, "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.", R.drawable.shawshank, "https://www.imdb.com/title/tt0111161/"),
                    Movie(0, "The Godfather", "Francis Ford Coppola", 1972, "The aging patriarch of an organized crime dynasty transfers control of his clan to his reluctant son.", R.drawable.godfather, "https://www.imdb.com/title/tt0068646/"),
                    Movie(0, "Inception", "Christopher Nolan", 2010, "A mind-bending thriller about dreams within dreams.", R.drawable.inception, "https://www.imdb.com/title/tt1375666/")

                )
                (application as App).database.movieDao().insertMovies(sampleMovies)
                Log.d("MainActivity", "Inserted ${sampleMovies.size} sample movies")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("hr.tvz.android.listalapenda.SHARE_ACTION")
        ContextCompat.registerReceiver(this, shareReceiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getBoolean("share_broadcast_sent", false)) {
            Toast.makeText(this, getString(R.string.share_action_received), Toast.LENGTH_SHORT).show()
            sharedPreferences.edit().putBoolean("share_broadcast_sent", false).apply()
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(shareReceiver)
    }

    fun displayMovieDetails(movie: Movie) {
        val fragment = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("movie", movie)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.detailFragmentContainer, fragment)
            .commit()
    }
}