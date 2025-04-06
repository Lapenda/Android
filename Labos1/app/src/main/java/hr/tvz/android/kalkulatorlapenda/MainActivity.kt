package hr.tvz.android.kalkulatorlapenda

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.kalkulatorlapenda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.yesButton.setOnClickListener(){
            goToNextScreen()
        }

        binding.noButton.setOnClickListener(){
            finish()
        }

        val spinner = binding.themeSpinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                applyTheme(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val savedTheme = sharedPreferences.getInt("theme_position", 0)
        spinner.setSelection(savedTheme)
        applyTheme(savedTheme)
    }

    private fun goToNextScreen(){
        val intent = Intent(this, NextActivity::class.java)
        startActivity(intent)
    }

    private fun applyTheme(themePosition: Int){
        when (themePosition) {
            0 -> {
                binding.main.setBackgroundColor(Color.WHITE)
                binding.textView.setTextColor(Color.BLACK)
                binding.yesButton.setBackgroundColor(Color.BLACK)
                binding.yesButton.setTextColor(Color.WHITE)
                binding.noButton.setBackgroundColor(Color.BLACK)
                binding.noButton.setTextColor(Color.WHITE)
            }
            1 -> {
                binding.main.setBackgroundColor(Color.DKGRAY)
                binding.textView.setTextColor(Color.WHITE)
                binding.yesButton.setBackgroundColor(Color.WHITE)
                binding.yesButton.setTextColor(Color.BLACK)
                binding.noButton.setBackgroundColor(Color.WHITE)
                binding.noButton.setTextColor(Color.BLACK)
            }
            2 -> {
                binding.main.setBackgroundColor(Color.parseColor("#00695C")) // Teal
                binding.textView.setTextColor(Color.WHITE)
                binding.yesButton.setBackgroundColor(Color.YELLOW)
                binding.yesButton.setTextColor(Color.BLACK)
                binding.noButton.setBackgroundColor(Color.YELLOW)
                binding.noButton.setTextColor(Color.BLACK)
            }
        }

        sharedPreferences.edit().putInt("theme_position", themePosition).apply()
    }

}