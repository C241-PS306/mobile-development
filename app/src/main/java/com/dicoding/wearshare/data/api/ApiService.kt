package com.dicoding.wearshare.data.api

import com.dicoding.wearshare.data.response.DetailPantiResponse
import com.dicoding.wearshare.data.response.DeteksiResponse
import com.dicoding.wearshare.data.response.LoginResponse
import com.dicoding.wearshare.data.response.PantiResponseItem
import com.dicoding.wearshare.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("list/panti-asuhan")
    fun getPanti(): Call<List<PantiResponseItem>>

    @GET("list/panti-asuhan/{id}")
    suspend fun getDetailPanti(
        @Path("id") id: String
    ): DetailPantiResponse

    @Multipart
    @POST("api/predict")
    suspend fun uploadImage(
        @Part("email") email: RequestBody,
        @Part file : MultipartBody.Part
    ): Response<DeteksiResponse>
}