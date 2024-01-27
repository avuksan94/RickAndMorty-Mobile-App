package hr.algebra.rickandmortyapp.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.rickandmortyapp.RickNMortyReceiver
import hr.algebra.rickandmortyapp.framework.sendBroadcast
import hr.algebra.rickandmortyapp.handler.downloadAndStore
import hr.algebra.rickandmortyapp.modelApi.RnMCharacter
import hr.algebra.rickandmortyapp.model.Character
import hr.algebra.rickandmortyapp.modelApi.RnMInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickNMortyFetcher(private val context: Context) {
    private val rickNMortyAPI: RickNMortyAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        rickNMortyAPI = retrofit.create(RickNMortyAPI::class.java)
    }

    /*
    fun fetchItems(count: Int) {
        println("FETCHING ITEMS!")
        val request = rickNMortyAPI.fetchItems(count = 10)

        request.enqueue(object: Callback<List<RnMCharacter>> {
            override fun onResponse(
                call: Call<List<RnMCharacter>>,
                response: Response<List<RnMCharacter>>
            ) {
                response.body()?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<List<RnMCharacter>>, t: Throwable) {
                Log.e(javaClass.name, t.toString(), t)
            }

        })
    }
     */

    fun fetchItems(page: Int) {
        println("FETCHING ITEMS!")
        val request = rickNMortyAPI.fetchItems(page = page)

        request.enqueue(object : Callback<RnMInfo> {
            override fun onResponse(
                call: Call<RnMInfo>,
                response: Response<RnMInfo>
            ) {
                if (response.isSuccessful) {
                    val characterInfo = response.body()
                    if (characterInfo != null) {
                        val characters = characterInfo.results
                        println("API Response: $characters")
                        populateItems(characters)
                    } else {
                        Log.e(javaClass.name, "API response body is null")
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e(javaClass.name, "API response unsuccessful. Code: ${response.code()}, Error: $errorBody")
                    } catch (e: Exception) {
                        Log.e(javaClass.name, "Error parsing error response", e)
                    }
                }
            }

            override fun onFailure(call: Call<RnMInfo>, t: Throwable) {
                Log.e(javaClass.name, "API request failed: ${t.message}", t)
            }
        })
    }

    private fun populateItems(rnMCharacters: List<RnMCharacter>) {
        println("POPULATING ITEMS - List size: ${rnMCharacters.size}")
        // FOREGROUND - do not go to internet!!!
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            // BACKGROUND
            rnMCharacters.forEach {
                val picturePath = downloadAndStore(context, it.image)

                val values = ContentValues().apply {
                    put(Character::name.name, it.name)
                    put(Character::status.name, it.status)
                    put(Character::species.name, it.species)
                    put(Character::type.name, it.type)
                    put(Character::gender.name, it.gender)
                    //put(Character::origin.name, "${it.origin.name}, ${it.origin.url}")
                    //put(Character::location.name, "${it.location.name}, ${it.location.url}")
                    put(Character::image.name, picturePath ?: "")
                    //put(Character::episode.name, it.episode.joinToString(", "))
                    put(Character::url.name, it.url)
                    put(Character::created.name, it.created)
                    put(Character::read.name, false)
                }
                println(it.name)
                context.contentResolver.insert(RNM_PROVIDER_CONTENT_URI, values)
            }
            context.sendBroadcast<RickNMortyReceiver>()
        }
    }

}