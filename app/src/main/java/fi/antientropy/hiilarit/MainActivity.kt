package fi.antientropy.hiilarit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
                    val foodData = loadFoodData(context)
                    FoodDataTable(modifier = Modifier.padding(innerPadding), foodData = foodData)
                }
            }
        }
    }
}

fun loadFoodData(context: Context): FoodData {
    val jsonString =
        context.resources.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
    return Gson().fromJson(jsonString, FoodData::class.java)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDataTable(modifier: Modifier = Modifier, foodData: FoodData) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { foodData.pages.size }
    )

    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                text = "${pagerState.currentPage + 1}/${foodData.pages.size}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Horizontal pager for the actual pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)  // Fill remaining space
        ) { page ->
            val foodPage = foodData.pages[page]
            FoodPageContent(foodPage)
        }
    }
}

@Composable
fun FoodPageContent(foodPage: FoodPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp),
        verticalArrangement = Arrangement.Top,

        ) {
        Text(text = foodPage.pageTitle, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            foodPage.data.forEach { foodItem ->
                Row {
                    Text(text = foodItem.Nimi, modifier = Modifier.weight(2f))
                    Text(text = foodItem.Määrä, modifier = Modifier.weight(1f))
                    Text(text = foodItem.Massa, modifier = Modifier.weight(1f))
                    Text(text = foodItem.Hiilihydraatit, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FoodTablePreview() {
    val context = LocalContext.current
    val foodData = loadFoodData(context = context)
    HiilaritTheme {
        FoodDataTable(foodData = foodData)
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