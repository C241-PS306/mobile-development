package com.dicoding.wearshare.ui.detailPanti

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.wearshare.data.repo.PantiRepository
import com.dicoding.wearshare.data.response.DetailPantiResponse

class DetailPantiViewModel(private val repository: PantiRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
    val user: LiveData<DetailPantiResponse> = repository.user

    fun getDetailPanti(id: String) {
        repository.getDetailPanti(id)
    }
}
