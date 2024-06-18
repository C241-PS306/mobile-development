package com.dicoding.wearshare.ui.listpanti

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.wearshare.data.adapter.PantiAdapter
import com.dicoding.wearshare.data.response.PantiResponseItem
import com.dicoding.wearshare.databinding.ActivityListPantiBinding
import com.dicoding.wearshare.ui.detailPanti.DetailPantiActivity
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory

class ListPantiActivity : AppCompatActivity(), PantiAdapter.RecyclerViewClickListener {
    private lateinit var binding: ActivityListPantiBinding
    private lateinit var adapter: PantiAdapter

    private val viewModel: ListPantiViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPantiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivActionBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()

        viewModel.pantiResponse.observe(this, { response ->
            response?.let {
                val filteredList = it.filterNotNull()
                showLoading(false)
                adapter.submitList(filteredList)
            }
        })

        findItems("")
    }

    private fun setupRecyclerView() {
        adapter = PantiAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, item: PantiResponseItem) {
        val intent = Intent(this@ListPantiActivity, DetailPantiActivity::class.java)
        intent.putExtra("EXTRA_ID", item.id)
        intent.putExtra("EXTRA_NAMA", item.namaPanti)
        intent.putExtra("EXTRA_GAMBAR", item.gambar)
        intent.putExtra("EXTRA_LOKASI", item.lokasi)
        startActivity(intent)
    }

    private fun findItems(string: String) {
        showLoading(true)
        viewModel.getPanti()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

