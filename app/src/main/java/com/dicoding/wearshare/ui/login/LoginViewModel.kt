package com.dicoding.wearshare.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wearshare.data.pref.UserModel
import com.dicoding.wearshare.data.repo.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel()  {

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}

