package hr.tvz.android.listalapenda.views.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.listalapenda.views.fragments.MovieDetailFragment
import hr.tvz.android.listalapenda.R
import hr.tvz.android.listalapenda.databinding.ActivityDetailBinding
import hr.tvz.android.listalapenda.models.Movie

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("SharePrefs", MODE_PRIVATE)
        val movie = intent.getParcelableExtra<Movie>("movie")
        Log.d("MovieAppDebug", "DetailActivity onCreate, movie: ${movie?.title}")

        if (movie == null) {
            Log.e("MovieAppDebug", "Movie extra is null")
            finish()
            return
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = movie.title
        }

        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (existingFragment == null) {
            Log.d("MovieAppDebug", "Creating new MovieDetailFragment")
            val fragment = MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("movie", movie)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commitAllowingStateLoss()
        } else {
            Log.d("MovieAppDebug", "Fragment already exists, skipping transaction")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                showShareDialog()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showShareDialog() {
        if (isFinishing || isDestroyed) return
        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.share))
                .setMessage(getString(R.string.share_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    sendShareBroadcast()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    private fun sendShareBroadcast() {
        val intent = Intent("hr.tvz.android.listalapenda.SHARE_ACTION")
        sendBroadcast(intent)
        sharedPreferences.edit().putBoolean("share_broadcast_sent", true).apply()
    }
}