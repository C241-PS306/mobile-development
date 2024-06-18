package com.dicoding.wearshare.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.wearshare.R
import com.dicoding.wearshare.databinding.ActivityMainBinding
import com.dicoding.wearshare.ui.detection.DetectionFragment
import com.dicoding.wearshare.ui.home.HomeFragment
import com.dicoding.wearshare.ui.profile.ProfileFragment
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory
import com.dicoding.wearshare.ui.welcome.WelcomeActivity


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    lateinit var homeFragment: HomeFragment
    lateinit var detectionFragment: DetectionFragment
    lateinit var profileFragment: ProfileFragment

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                loadFragment(homeFragment)
            }
        }

        homeFragment = HomeFragment()
        detectionFragment = DetectionFragment()
        profileFragment = ProfileFragment()

        binding.btmNavBar.setOnItemSelectedListener {
            try {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        loadFragment(homeFragment)
                        true
                    }
                    R.id.navigation_detection -> {
                        loadFragment(detectionFragment)
                        true
                    }
                    R.id.navigation_profile -> {
                        loadFragment(profileFragment)
                        true
                    }
                    else -> false
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
