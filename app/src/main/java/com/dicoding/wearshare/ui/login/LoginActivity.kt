package com.dicoding.wearshare.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            loginButton.setOnClickListener {
                when{
                    binding.edLoginEmail.text.toString().isEmpty() -> {
                        binding.edLoginEmail.error = getString(R.string.error_empty_field)
                    }

                    binding.edLoginPassword.text.toString().isEmpty() -> {
                        binding.edLoginPassword.error = getString(R.string.error_empty_field)
                    }

                    binding.edLoginPassword.text.toString().length < 8 -> {
                        binding.edLoginPassword.error = getString(R.string.error_short_password)
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
                        viewModel.saveSession(UserModel(email, result.data.loginResult.token))
                        showAlertDialog(
                            getString(R.string.success_title),
                            result.data.message,
                            getString(R.string.continue_title),
                            MainActivity::class.java,
                            result.data.loginResult.name
                        )
                        showLoading(false)
                    }

                    is ResultValue.Error -> {
                        showAlertDialog(
                            getString(R.string.failed_title),
                            result.error,
                            getString(R.string.try_again),
                            MainActivity::class.java,
                            ""
                        )
                        showLoading(false)
                    }
                }
            }
        }
    }

    companion object {
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
    }



    private fun showAlertDialog(
        title: String,
        message: String,
        textButton: String,
        targetActivity: Class<*>? = LoginActivity::class.java,
        extra: String? = null
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textButton) { _, _ ->
                val intent = Intent(this@LoginActivity, targetActivity)
                intent.putExtra("name", extra)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
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