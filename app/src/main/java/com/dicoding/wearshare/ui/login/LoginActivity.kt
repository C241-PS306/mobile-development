package com.dicoding.wearshare.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.wearshare.ui.main.MainActivity
import com.dicoding.wearshare.R
import com.dicoding.wearshare.data.pref.ResultValue
import com.dicoding.wearshare.data.pref.UserModel
import com.dicoding.wearshare.databinding.ActivityLoginBinding
import com.dicoding.wearshare.ui.register.RegisterActivity
import com.dicoding.wearshare.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        with(binding){
            loginButton.setOnClickListener {
                when{
                    edLoginEmail.text.toString().isEmpty() -> {
                        edLoginEmail.error = getString(R.string.error_empty_field)
                    }

                    edLoginPassword.text.toString().isEmpty() -> {
                        edLoginPassword.error = getString(R.string.error_empty_field)
                    }

                    edLoginPassword.text.toString().length < 8 -> {
                        edLoginPassword.error = getString(R.string.error_short_password)
                    }

                    else -> login()
                }
            }
            tvSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        viewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultValue.Loading -> {
                        showLoading(true)
                    }

                    is ResultValue.Success -> {
                        val username = result.data.user.username.toString() // Convert to String if necessary
                        viewModel.saveSession(UserModel(email, result.data.user.password))
                        showLoading(false)
                        showAlertDialog(
                            getString(R.string.success_title),
                            getString(R.string.success_title),
                            getString(R.string.continue_title),
                            MainActivity::class.java, username
                        )
                    }

                    is ResultValue.Error -> {
                        Log.e("LoginActivity", "Error: ${result.error}")
                        showAlertDialog(
                            getString(R.string.failed_title),
                            result.error,
                            getString(R.string.try_again)
                        )
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun navigateToMainActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("name", userName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    companion object {
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        textButton: String,
        targetActivity: Class<*>? = null,
        userName: String? = null
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textButton) { _, _ ->
                targetActivity?.let {
                    val intent = Intent(this@LoginActivity, it).apply {
                        userName?.let { name -> putExtra("name", name) }
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
            create()
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
