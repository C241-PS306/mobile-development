package com.dicoding.wearshare.data.di

import android.content.Context
import com.dicoding.wearshare.data.api.ApiConfig
import com.dicoding.wearshare.data.pref.UserPreference
import com.dicoding.wearshare.data.pref.dataStore
import com.dicoding.wearshare.data.repo.PantiRepository
import com.dicoding.wearshare.data.repo.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService,pref)
    }

    fun providePantiRepository(): PantiRepository {
        val apiService = ApiConfig.getApiService()
        return PantiRepository(apiService)
    }
}

