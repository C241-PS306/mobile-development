package com.dicoding.wearshare.data.repo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.wearshare.data.api.ApiService
import com.dicoding.wearshare.data.response.DetailPantiResponse
import com.dicoding.wearshare.data.response.PantiResponseItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PantiRepository(private val apiService: ApiService) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<DetailPantiResponse>()
    val user: LiveData<DetailPantiResponse> = _user

    fun getPanti(
        callback: (List<PantiResponseItem>) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        apiService.getPanti().enqueue(object : Callback<List<PantiResponseItem>> {
            override fun onResponse(
                call: Call<List<PantiResponseItem>>,
                response: Response<List<PantiResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val pantiResponseItems = response.body()
                    if (pantiResponseItems != null) {
                        callback(pantiResponseItems)
                    } else {
                        errorCallback(Throwable("Response body is null"))
                    }
                } else {
                    val errorMessage = response.message() ?: "Unknown error"
                    errorCallback(Throwable(errorMessage))
                }
            }

            override fun onFailure(call: Call<List<PantiResponseItem>>, t: Throwable) {
                Log.d("RESPON", "onFailure: $t")
                errorCallback(t)
            }
        })
    }

    fun getDetailPanti(id: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("PantiRepository", "Requesting detail panti for id: $id")
                val response = apiService.getDetailPanti(id)
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _user.value = response
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    Log.e(TAG, "Failed to get detail panti: ${e.message}")
                }
            }
        }
    }
}

