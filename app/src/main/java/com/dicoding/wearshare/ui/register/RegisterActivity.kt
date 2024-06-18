package com.dicoding.wearshare.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.wearshare.R
import com.dicoding.wearshare.data.pref.ResultValue
import com.dicoding.wearshare.databinding.ActivityRegisterBinding
import com.dicoding.wearshare.ui.login.LoginActivity
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        with(binding) {
            signupButton.setOnClickListener {
                when {
                    binding.edRegisterName.text.toString().isEmpty() -> {
                        binding.edRegisterName.error = getString(R.string.error_empty_field)
                    }
                    binding.edRegisterEmail.text.toString().isEmpty() -> {
                        binding.edRegisterEmail.error = getString(R.string.error_empty_field)
                    }
                    binding.edRegisterPassword.text.toString().isEmpty() -> {
                        binding.edRegisterPassword.error = getString(R.string.error_empty_field)
                    }
                    binding.edRegisterPassword.text.toString().length < 8 -> {
                        binding.edRegisterPassword.error = getString(R.string.error_short_password)
                    }
                    else -> register()
                }
            }

            accLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
        playAnimation()
    }

    private fun register() {

        val username = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        viewModel.register(username, email, password)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultValue.Loading -> {
                            showLoading(true)
                        }
                        is ResultValue.Success -> {
                            showLoading(false)
                            showAlertDialog(
                                getString(R.string.success_title),
                                result.data.toString(),
                                getString(R.string.login),
                                LoginActivity::class.java
                            )
                        }
                        is ResultValue.Error -> {
                            showLoading(false)
                            showAlertDialog(
                                getString(R.string.failed_title),
                                result.error,
                                getString(R.string.try_again)
                            )
                        }
                    }
                }
            }
    }


    private fun showAlertDialog(
        title: String,
        message: String,
        textButton: String,
        targetActivity: Class<*>? = RegisterActivity::class.java
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textButton) { _, _ ->
                val intent = Intent(this@RegisterActivity, targetActivity)
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

