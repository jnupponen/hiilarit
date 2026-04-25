package fi.antientropy.hiilarit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
@Suppress("LongMethod")
fun FoodSearch(
    foodData: FoodData,
    onItemSelected: (pageIndex: Int, itemIndex: Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
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
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        focusManager.clearFocus()
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = {
                        Text(
                            text = "Etsi ruokaa...",
                            maxLines = 1
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
        ) {
            LazyColumn {
                items(filteredItems) { (pageIndex, itemIndex, foodItem) ->
                    Text(
                        text = foodItem.nimi,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemSelected(pageIndex, itemIndex)
                                focusManager.clearFocus()
                                expanded = false
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
