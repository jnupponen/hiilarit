package fi.antientropy.hiilarit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Displays a Material 3 SearchBar to search across all FoodItem.nimi fields.
 *
 * @param foodData The complete data (all pages) to search through.
 * @param onItemSelected Callback that provides (pageIndex, itemIndex) when a matching food item is clicked.
 *                       Can be used to navigate to the corresponding page in the pager and highlight that row.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearch(
    foodData: FoodData,
    onItemSelected: (pageIndex: Int, itemIndex: Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Flatten all pages into a single list so we can filter easily.
    // Triple(pageIndex, itemIndex, item)
    val allItems = remember(foodData) {
        foodData.pages.flatMapIndexed { pageIndex, page ->
            page.data.mapIndexed { itemIndex, item ->
                Triple(pageIndex, itemIndex, item)
            }
        }
    }

    // Whenever query changes, derive a new list of filtered items
    val filteredItems = remember(query) {
        if (query.isBlank()) {
            emptyList()
        } else {
            allItems.filter { triple ->
                triple.third.nimi.contains(query, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Material3 SearchBar (Experimental API)
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                // Called when the user hits the search keyboard action
                // Optionally close the search to show main content
                focusManager.clearFocus()
                active = false
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = "Etsi ruokaa...")
            },
            modifier = Modifier.fillMaxWidth(),
            // Optional icons:
            leadingIcon = { /* Add a leading icon if you wish */ },
            trailingIcon = { /* Add a trailing icon if you wish */ }
        ) {
            // When the search bar is active, show the real-time results in a LazyColumn
            LazyColumn {
                items(filteredItems) { (pageIndex, itemIndex, foodItem) ->
                    Text(
                        text = foodItem.nimi,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // When a search result is clicked:
                                onItemSelected(pageIndex, itemIndex)
                                // Hide keyboard, collapse SearchBar
                                focusManager.clearFocus()
                                active = false
                            }
                            .padding(8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
