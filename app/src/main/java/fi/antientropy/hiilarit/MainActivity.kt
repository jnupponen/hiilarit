package fi.antientropy.hiilarit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.antientropy.hiilarit.ui.theme.HiilaritTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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
    return Json.decodeFromString(jsonString)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDataTable(
    modifier: Modifier = Modifier,
    foodData: FoodData
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { foodData.pages.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        // Top row: Page indicator (e.g. "1/17")
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

        // HorizontalPager for pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val foodPage = foodData.pages[page]
            FoodPageContent(foodPage)
        }

        // Accordion tag cloud at the bottom
        AccordionTagCloud(
            foodData = foodData,
            onPageSelected = { index ->
                // Animate scroll to selected page
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
    }
}

@Composable
fun FoodPageContent(foodPage: FoodPage) {
    var selectedItemIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = foodPage.pageTitle, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            foodPage.data.forEachIndexed { index, foodItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedItemIndex = index }
                        .background(
                            if (selectedItemIndex == index) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                Color.Transparent
                            }
                        )
                ) {
                    Text(text = foodItem.nimi, modifier = Modifier.weight(2f))
                    Text(text = foodItem.maara, modifier = Modifier.weight(1f))
                    Text(text = foodItem.massa, modifier = Modifier.weight(1f))
                    Text(text = foodItem.hiilihydraatit, modifier = Modifier.weight(1f))
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

