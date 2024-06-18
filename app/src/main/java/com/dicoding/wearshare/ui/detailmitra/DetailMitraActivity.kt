package com.dicoding.wearshare.ui.detailmitra

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.wearshare.databinding.ActivityDetailMitraBinding

class DetailMitraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMitraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivActionBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val phoneNumber = "6285921688921"

        binding.contactButton.setOnClickListener {
            openWhatsAppContact(phoneNumber)
        }
    }

    private fun openWhatsAppContact(phoneNumber: String) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error opening WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}
