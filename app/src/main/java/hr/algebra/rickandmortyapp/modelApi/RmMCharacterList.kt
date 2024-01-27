package hr.algebra.rickandmortyapp.modelApi

import com.google.gson.annotations.SerializedName

data class RmMCharacterList(
    @SerializedName("info") val info: RnMInfoDetails,
    @SerializedName("results") val results: List<RnMCharacter>
)
