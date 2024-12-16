package fi.antientropy.hiilarit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import fi.antientropy.hiilarit.ui.theme.HiilaritTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HiilaritTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    FoodDataTable(modifier = Modifier.padding(innerPadding), context = context)
                }
            }
        }
    }
}


@Composable
fun FoodDataTable(modifier: Modifier = Modifier, context: Context) {
    val context = LocalContext.current
    val jsonString =
        context.resources.openRawResource(R.raw.data).bufferedReader().use { it.readText() }

    val foodData = Gson().fromJson<FoodData>(jsonString, FoodData::class.java)
    val elintarvikkeetPage = foodData.pages.find { it.pageTitle == "Elintarvikkeet" }

    Column(modifier = modifier) {
        if (elintarvikkeetPage != null) {
            Text(
                text = elintarvikkeetPage.pageTitle,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                elintarvikkeetPage.data.forEach { foodItem ->
                    Row {
                        Text(text = foodItem.Nimi, modifier = Modifier.weight(1f))
                        Text(text = foodItem.Määrä, modifier = Modifier.weight(1f))
                        Text(text = foodItem.Massa, modifier = Modifier.weight(1f))
                        Text(text = foodItem.Hiilihydraatit, modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            Text(text = "Elintarvikkeet page not found")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodTablePreview() {
    HiilaritTheme {
        val context = LocalContext.current
        FoodDataTable(context = context)
    }
}

data class FoodItem(
    val Nimi: String,
    val Määrä: String,
    val Massa: String,
    val Hiilihydraatit: String
)

data class FoodPage(
    val pageTitle: String,
    val data: List<FoodItem>
)

data class FoodData(
    val pages: List<FoodPage>
)