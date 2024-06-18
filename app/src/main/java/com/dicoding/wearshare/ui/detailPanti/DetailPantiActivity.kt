package com.dicoding.wearshare.ui.detailPanti

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.wearshare.data.response.DetailPantiResponse
import com.dicoding.wearshare.databinding.ActivityDetailPantiBinding
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory

class DetailPantiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPantiBinding
    lateinit var dataViewModel: DetailPantiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPantiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber = "6285921688921"

        binding.contactButton.setOnClickListener {
            openWhatsAppContact(phoneNumber)
        }

        binding.ivActionBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val id = intent.getStringExtra("EXTRA_ID")

        if (id != null) {
            // Gunakan ViewModelFactory untuk membuat DetailPantiViewModel
            val factory = ViewModelFactory.getInstance(this)
            dataViewModel = ViewModelProvider(this, factory).get(DetailPantiViewModel::class.java)

            dataViewModel.getDetailPanti(id)
            dataViewModel.user.observe(this) { ItemsItem ->
                setUserData(ItemsItem)
            }

            dataViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        } else {
            // Tangani kasus ketika id null
            Log.e("DetailPantiActivity", "ID is null")
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

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setUserData(items: DetailPantiResponse) {
        binding.titleTextView.text = items.namaPanti
        binding.descriptionTextView.text = items.deskripsi
        binding.locationTextView.text = items.lokasi
        Glide.with(this@DetailPantiActivity)
            .load(items.gambar)
            .apply(RequestOptions().override(1000, 800))
            .into(binding.imageView)
    }
}
