package fi.antientropy.hiilarit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodData(val pages: List<FoodPage>)

@Serializable
data class FoodPage(val pageTitle: String, val data: List<FoodItem>)

@Serializable
data class FoodItem(
    @SerialName("Nimi")
    val nimi: String,
    @SerialName("Määrä")
    val maara: String,
    @SerialName("Massa")
    val massa: String,
    @SerialName("Hiilihydraatit")
    val hiilihydraatit: String
)
