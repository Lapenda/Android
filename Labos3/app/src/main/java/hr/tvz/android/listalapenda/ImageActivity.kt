package hr.tvz.android.listalapenda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.listalapenda.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageResId = intent.getIntExtra("imageResId", 0)
        try {
            if (imageResId != 0) {
                binding.image.setImageResource(imageResId)
            } else {
                binding.image.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } catch (e: Exception) {
            binding.image.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }
}