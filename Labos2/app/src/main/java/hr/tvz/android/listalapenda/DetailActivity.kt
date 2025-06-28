package hr.tvz.android.listalapenda

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.listalapenda.databinding.ActivityDetailBinding
import hr.tvz.android.listalapenda.models.Movie

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var movie: Movie
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("SharePrefs", MODE_PRIVATE)

        movie = intent.getParcelableExtra("movie") ?: return

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = movie.title
        }

        displayMovieDetails()
        setupImageClick()
        setupExternalActivity()
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

    private fun displayMovieDetails() {
        binding.titleTextView.text = movie.title
        binding.directorTextView.text = movie.director
        binding.yearTextView.text = movie.year.toString()
        binding.descriptionTextView.text = movie.description
        try {
            binding.imageView.setImageResource(movie.imageResId)
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.imageView.startAnimation(fadeIn)
        } catch (e: Exception) {
            binding.imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.imageView.startAnimation(fadeIn)
        }
    }

    private fun setupImageClick() {
        binding.imageView.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java).apply {
                putExtra("imageResId", movie.imageResId)
            }
            startActivity(intent)
        }
    }

    private fun setupExternalActivity() {
        binding.openWebsiteButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.url))
            startActivity(intent)
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