package com.dicoding.wearshare.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wearshare.data.di.Injection
import com.dicoding.wearshare.data.repo.PantiRepository
import com.dicoding.wearshare.data.repo.UserRepository
import com.dicoding.wearshare.ui.detailPanti.DetailPantiViewModel
import com.dicoding.wearshare.ui.listpanti.ListPantiViewModel
import com.dicoding.wearshare.ui.login.LoginViewModel
import com.dicoding.wearshare.ui.main.MainViewModel
import com.dicoding.wearshare.ui.profile.ProfileViewModel
import com.dicoding.wearshare.ui.register.RegisterViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val pantiRepository: PantiRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ListPantiViewModel::class.java) -> {
                ListPantiViewModel(pantiRepository) as T
            }
            modelClass.isAssignableFrom(DetailPantiViewModel::class.java) -> {
                DetailPantiViewModel(pantiRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.providePantiRepository()
                )
            }.also { instance = it }
    }
}
