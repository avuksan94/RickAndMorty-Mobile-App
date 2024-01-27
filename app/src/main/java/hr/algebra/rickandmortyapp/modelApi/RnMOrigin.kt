package hr.algebra.rickandmortyapp.modelApi

import com.google.gson.annotations.SerializedName

data class RnMOrigin(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
