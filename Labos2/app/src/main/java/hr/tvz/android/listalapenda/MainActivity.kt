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
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.listalapenda.databinding.ActivityMainBinding
import hr.tvz.android.listalapenda.models.Movie

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var movieList: List<Movie>
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

        sharedPreferences = getSharedPreferences("SharePrefs", MODE_PRIVATE)

        loadMovies()
        setupRecyclerView()
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

    private fun loadMovies() {
        val titles = resources.getStringArray(R.array.movie_titles)
        val directors = resources.getStringArray(R.array.movie_directors)
        val years = resources.getStringArray(R.array.movie_years).map { it.toInt() }
        val descriptions = resources.getStringArray(R.array.movie_descriptions)
        val images = resources.getStringArray(R.array.movie_images)
        val urls = resources.getStringArray(R.array.movie_urls)

        if (titles.size != directors.size || directors.size != years.size || years.size != descriptions.size || descriptions.size != images.size || images.size != urls.size) {
            Toast.makeText(this, "Error: Resource arrays must have the same length", Toast.LENGTH_LONG).show()
            return
        }

        movieList = titles.indices.map { i ->
            val imageResId = resources.getIdentifier(images[i], "drawable", packageName)
            val validImageResId = if (imageResId != 0) imageResId else android.R.drawable.ic_menu_gallery
            Movie(titles[i], directors[i], years[i], descriptions[i], validImageResId, urls[i])
        }
    }

    private fun setupRecyclerView() {
        val adapter = MovieAdapter(movieList) { movie ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("movie", movie)
            }
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}