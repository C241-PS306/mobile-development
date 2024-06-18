package com.dicoding.wearshare.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.wearshare.R
import com.dicoding.wearshare.data.adapter.PantiAdapter
import com.dicoding.wearshare.data.response.PantiResponseItem
import com.dicoding.wearshare.databinding.FragmentHomeBinding
import com.dicoding.wearshare.ui.detailPanti.DetailPantiActivity
import com.dicoding.wearshare.ui.detailmitra.DetailMitraActivity
import com.dicoding.wearshare.ui.listpanti.ListPantiActivity
import com.dicoding.wearshare.ui.listpanti.ListPantiViewModel
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory

class HomeFragment : Fragment(), PantiAdapter.RecyclerViewClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListPantiViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: PantiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupViews()

        binding.tvSeeAll.setOnClickListener {
            val intent = Intent(context, ListPantiActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewMitra2.setOnClickListener {
            val intent = Intent(context, DetailMitraActivity::class.java)
            intent.putExtra("mitra_id", "ID_MITRA_ANDA")
            startActivity(intent)
        }

        viewModel.pantiResponse.observe(viewLifecycleOwner, { response ->
            response?.let {
                val filteredList = it.filterNotNull().take(3)
                adapter.submitList(filteredList)
            }
        })

        findItems("")

        return root
    }

    private fun setupViews() {
        binding.tvWelcome.text = getString(R.string.message_welcome)
        binding.tvSubtitle.text = getString(R.string.message_welcome2)

        binding.ivBanner.setImageResource(R.drawable.gambar_dashboard)
        setupRecyclerView()

        binding.name2.text = getString(R.string.mitra_2)
        binding.location2.text = getString(R.string.lokasi_mitra)
        binding.imgAvatar2.setImageResource(R.drawable.logo_mitra)
    }

    private fun setupRecyclerView() {
        adapter = PantiAdapter(this)
        binding.rvPantiAsuhan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPantiAsuhan.adapter = adapter
    }

    override fun onItemClick(view: View, item: PantiResponseItem) {
        val intent = Intent(context, DetailPantiActivity::class.java)
        intent.putExtra("EXTRA_ID", item.id)
        intent.putExtra("EXTRA_NAMA", item.namaPanti)
        intent.putExtra("EXTRA_GAMBAR", item.gambar)
        intent.putExtra("EXTRA_LOKASI", item.lokasi)
        startActivity(intent)
    }

    private fun findItems(string: String) {
        viewModel.getPanti()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
