package com.dicoding.wearshare.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.wearshare.data.pref.ResultValue
import com.dicoding.wearshare.data.repo.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<ResultValue<Any>> {
        return repository.register(name, email, password)
    }
}