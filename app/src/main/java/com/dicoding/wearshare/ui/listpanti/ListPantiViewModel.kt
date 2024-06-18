package com.dicoding.wearshare.ui.listpanti

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.wearshare.data.response.PantiResponseItem
import com.dicoding.wearshare.data.repo.PantiRepository

class ListPantiViewModel(private val repository: PantiRepository) : ViewModel() {
    private val _pantiResponse = MutableLiveData<List<PantiResponseItem?>>()
    val pantiResponse: LiveData<List<PantiResponseItem?>>
        get() = _pantiResponse

    fun getPanti() {
        repository.getPanti(
            { items -> _pantiResponse.value = items },
            { throwable -> _pantiResponse.value = listOf() }
        )
    }
}

