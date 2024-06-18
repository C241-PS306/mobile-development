package com.dicoding.wearshare.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.wearshare.data.pref.UserModel
import com.dicoding.wearshare.data.repo.UserRepository

class MainViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun login(email: String, password: String) = userRepository.login(email, password)

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

}