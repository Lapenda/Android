package hr.tvz.android.kalkulatorlapenda

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.kalkulatorlapenda.databinding.ActivityMainBinding
import hr.tvz.android.kalkulatorlapenda.databinding.ActivityNextBinding
import java.math.BigDecimal
import java.math.RoundingMode

class NextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNextBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNextBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.main)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val liters = binding.editTextNumber2
        val kilometers = binding.editTextNumber

        binding.calculateBtn.setOnClickListener(){
            val litersValue = liters.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO
            val kilometersValue = kilometers.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO

            Log.d("CalculateApp", "Liters: $litersValue")
            Log.d("CalculateApp", "Kilometers: $kilometersValue")

            calculate(litersValue, kilometersValue)
        }

        binding.goBackBtn.setOnClickListener(){
            finish()
        }

        applyTheme(sharedPreferences.getInt("theme_position", 0))
    }

    private fun calculate(liters: BigDecimal, kilometers: BigDecimal){
        if (kilometers == BigDecimal.ZERO) {
            val kilometersError = R.string.kilometersError
            Toast.makeText(this, kilometersError, Toast.LENGTH_SHORT).show()
            return
        }

        if(liters == BigDecimal.ZERO){
            val litersError = R.string.litersError
            Toast.makeText(this, litersError, Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("CalculateApp", "Liters 2: $liters")
        Log.d("CalculateApp", "Kilometers 2: $kilometers")

        val result = liters
            .divide(kilometers, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP)

        Log.d("CalculateApp", "Result: $result")

        binding.textView5.text = result.toString()
    }

    private fun applyTheme(themePosition: Int){
        when (themePosition) {
            0 -> {
                binding.main.setBackgroundColor(Color.WHITE)
                binding.textViewKm.setTextColor(Color.BLACK)
                binding.textView3.setTextColor(Color.BLACK)
                binding.answerTextView.setTextColor(Color.BLACK)
                binding.textView5.setTextColor(Color.BLACK)
                binding.editTextNumber.setTextColor(Color.BLACK)
                binding.editTextNumber.setHintTextColor(Color.GRAY)
                binding.editTextNumber2.setTextColor(Color.BLACK)
                binding.editTextNumber2.setHintTextColor(Color.GRAY)
                binding.calculateBtn.setBackgroundColor(Color.BLACK)
                binding.calculateBtn.setTextColor(Color.WHITE)
                binding.goBackBtn.setBackgroundColor(Color.BLACK)
                binding.goBackBtn.setTextColor(Color.WHITE)
            }
            1 -> {
                binding.main.setBackgroundColor(Color.DKGRAY)
                binding.textViewKm.setTextColor(Color.WHITE)
                binding.textView3.setTextColor(Color.WHITE)
                binding.answerTextView.setTextColor(Color.WHITE)
                binding.textView5.setTextColor(Color.WHITE)
                binding.editTextNumber.setTextColor(Color.WHITE)
                binding.editTextNumber.setHintTextColor(Color.LTGRAY)
                binding.editTextNumber2.setTextColor(Color.WHITE)
                binding.editTextNumber2.setHintTextColor(Color.LTGRAY)
                binding.calculateBtn.setBackgroundColor(Color.WHITE)
                binding.calculateBtn.setTextColor(Color.BLACK)
                binding.goBackBtn.setBackgroundColor(Color.WHITE)
                binding.goBackBtn.setTextColor(Color.BLACK)
            }
            2 -> {
                binding.main.setBackgroundColor(Color.parseColor("#00695C"))
                binding.textViewKm.setTextColor(Color.WHITE)
                binding.textView3.setTextColor(Color.WHITE)
                binding.answerTextView.setTextColor(Color.WHITE)
                binding.textView5.setTextColor(Color.WHITE)
                binding.editTextNumber.setTextColor(Color.WHITE)
                binding.editTextNumber.setHintTextColor(Color.LTGRAY)
                binding.editTextNumber2.setTextColor(Color.WHITE)
                binding.editTextNumber2.setHintTextColor(Color.LTGRAY)
                binding.calculateBtn.setBackgroundColor(Color.YELLOW)
                binding.calculateBtn.setTextColor(Color.BLACK)
                binding.goBackBtn.setBackgroundColor(Color.YELLOW)
                binding.goBackBtn.setTextColor(Color.BLACK)
            }
        }
    }
}