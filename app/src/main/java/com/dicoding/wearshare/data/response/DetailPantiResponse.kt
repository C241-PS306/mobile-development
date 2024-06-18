package com.dicoding.wearshare.data.response

import com.google.gson.annotations.SerializedName

data class DetailPantiResponse(

    @field:SerializedName("Alamat")
    val alamat: String? = null,

    @field:SerializedName("Lokasi")
    val lokasi: String? = null,

    @field:SerializedName("Nama Panti")
    val namaPanti: String? = null,

    @field:SerializedName("Gmaps")
    val gmaps: String? = null,

    @field:SerializedName("Deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("location")
    val location: Location? = null,

    @field:SerializedName("id")
    val id: String?,

    @field:SerializedName("No.Telp")
    val noTelp: String? = null,

    @field:SerializedName("gambar")
    val gambar: String? = null
)
