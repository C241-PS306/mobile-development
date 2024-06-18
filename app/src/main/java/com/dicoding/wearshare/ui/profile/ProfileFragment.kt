package com.dicoding.wearshare.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wearshare.R
import com.dicoding.wearshare.data.di.Injection
import com.dicoding.wearshare.databinding.FragmentProfileBinding
import com.dicoding.wearshare.ui.bantuan.BantuanActivity
import com.dicoding.wearshare.ui.kebijakanprivasi.KebijakanPrivasiActivity
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRepository = Injection.provideRepository(requireContext())
        val pantiRepository = Injection.providePantiRepository()
        val factory = ViewModelFactory(userRepository, pantiRepository)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        setupButtonClickListeners()

        viewModel.getUserSession().observe(viewLifecycleOwner) { user ->
            binding.profileEmail.text = user.email
        }
    }

    private fun setupButtonClickListeners() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            showToast(getString(R.string.logout_success))
        }

        binding.bantuan.setOnClickListener {
            val intent = Intent(activity, BantuanActivity::class.java)
            startActivity(intent)
        }
        binding.kebijakanpriv.setOnClickListener {
            val intent = Intent(activity, KebijakanPrivasiActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
