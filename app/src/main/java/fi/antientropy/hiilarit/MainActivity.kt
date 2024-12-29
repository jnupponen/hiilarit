package fi.antientropy.hiilarit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import fi.antientropy.hiilarit.ui.theme.HiilaritTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun App() {
    HiilaritTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            // Use contentWindowInsets to avoid the topBar reserving space.
            contentWindowInsets = WindowInsets.safeDrawing.only(
                WindowInsetsSides.Bottom + WindowInsetsSides.Start + WindowInsetsSides.End
            )
        ) { innerPadding ->
            val context = LocalContext.current
            val foodData = loadFoodData(context)
            MainView(modifier = Modifier.padding(innerPadding), foodData = foodData)
        }
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

    // This map keeps track of which item is selected for each page
    // e.g. selectedItems[pageIndex] = itemIndex
    val selectedItems = remember { mutableStateMapOf<Int, Int?>() }

    Column(modifier = modifier.fillMaxSize()) {
        // Top search bar
        FoodSearch(
            foodData = foodData,
            onItemSelected = { pageIndex, itemIndex ->
                // Highlight the clicked item
                selectedItems[pageIndex] = itemIndex
                // Navigate (animate) the pager to that page
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pageIndex)
                }
            }
        )

        // HorizontalPager for pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val foodPage = foodData.pages[page]
            FoodPageContent(
                foodPage = foodPage,
                currentPage = pagerState.currentPage,
                totalPages = foodData.pages.size,
                selectedItemIndex = selectedItems[page],
                onItemSelected = { newIndex ->
                    // If the user taps in the list, highlight that item
                    selectedItems[page] = newIndex
                }
            )
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
