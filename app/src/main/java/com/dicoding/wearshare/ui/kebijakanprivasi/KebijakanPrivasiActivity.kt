package com.dicoding.wearshare.ui.kebijakanprivasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.wearshare.databinding.ActivityKebijakanPrivasiBinding

class KebijakanPrivasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKebijakanPrivasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKebijakanPrivasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}