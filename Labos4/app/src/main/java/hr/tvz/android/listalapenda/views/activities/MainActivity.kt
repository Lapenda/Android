package hr.tvz.android.listalapenda.views.activities

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Build
import android.Manifest
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.lifecycleScope
import hr.tvz.android.listalapenda.views.fragments.MovieDetailFragment
import hr.tvz.android.listalapenda.R
import hr.tvz.android.listalapenda.app.App
import android.media.MediaPlayer


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var mediaPlayer: MediaPlayer

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

        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}