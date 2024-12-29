package fi.antientropy.hiilarit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.antientropy.hiilarit.ui.theme.HiilaritTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HiilaritTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val foodData = loadFoodData(context)
                    MainView(modifier = Modifier.padding(innerPadding), foodData = foodData)
                }
            }
        }
    }
}

@Composable
fun PageIndicator(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "${currentPage + 1}/$totalPages",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(
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
        PageIndicator(
            currentPage = pagerState.currentPage,
            totalPages = foodData.pages.size
        ) // Use PageIndicator

        // HorizontalPager for pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val foodPage = foodData.pages[page]
            FoodPageContent(foodPage)
        }

        // Accordion tag cloud at the bottom
        FoodPageTitleAccordion(
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

@Preview(showBackground = true)
@Composable
fun FoodTablePreview() {
    val context = LocalContext.current
    val foodData = loadFoodData(context = context)
    HiilaritTheme {
        MainView(foodData = foodData)
    }
}
