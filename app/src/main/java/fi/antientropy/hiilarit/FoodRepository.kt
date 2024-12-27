package fi.antientropy.hiilarit

import android.content.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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

fun loadFoodData(context: Context): FoodData {
    val jsonString =
        context.resources.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
    return Json.decodeFromString(jsonString)
}
