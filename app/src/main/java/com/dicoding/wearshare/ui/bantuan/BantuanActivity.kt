package com.dicoding.wearshare.ui.bantuan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.wearshare.databinding.ActivityBantuanBinding

class BantuanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBantuanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBantuanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}