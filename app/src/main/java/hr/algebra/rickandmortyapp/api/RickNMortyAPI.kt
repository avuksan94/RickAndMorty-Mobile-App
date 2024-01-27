package hr.algebra.rickandmortyapp.api

import hr.algebra.rickandmortyapp.modelApi.RnMCharacter
import hr.algebra.rickandmortyapp.modelApi.RnMInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://rickandmortyapi.com/api/"

interface RickNMortyAPI {
    @GET("character/")
    fun fetchItems(@Query("page") page: Int): Call<RnMInfo>
}
