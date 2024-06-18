package com.dicoding.wearshare.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.wearshare.data.api.ApiService
import com.dicoding.wearshare.data.pref.ResultValue
import com.dicoding.wearshare.data.pref.UserModel
import com.dicoding.wearshare.data.pref.UserPreference
import com.dicoding.wearshare.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
        instance = null
    }

    fun register(username: String, email: String, password: String): LiveData<ResultValue<Any>> {
        return liveData {
            emit(ResultValue.Loading)
            try {
                val successResponse = apiService.register(username, email, password).message
                emit(ResultValue.Success(successResponse))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(jsonInString, String::class.java)
                Log.e("UserRepository", "HttpException: $errorMessage")
                emit(ResultValue.Error(errorMessage ?: "Unknown error"))
            } catch (e: Exception) {
                Log.e("UserRepository", "Exception: ${e.message}")
                emit(ResultValue.Error("An error occurred"))
            }
        }
    }

    fun login(email: String, password: String): LiveData<ResultValue<LoginResponse>> {
        return liveData {
            emit(ResultValue.Loading)
            try {
                val successResponse = apiService.login(email, password)
                emit(ResultValue.Success(successResponse))
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                Log.e("UserRepository", "HttpException: $errorMessage")
                emit(ResultValue.Error(errorMessage ?: "Unknown error"))
            } catch (e: Exception) {
                Log.e("UserRepository", "Exception: ${e.message}")
                emit(ResultValue.Error("An error occurred"))
            }
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
