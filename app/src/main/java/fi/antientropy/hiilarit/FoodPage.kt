package fi.antientropy.hiilarit

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

        LazyColumn {
            itemsIndexed(foodPage.data) { index, foodItem ->
                // Pass selectedItemIndex to FoodRow
                FoodRow(selectedItemIndex, index, foodItem) { newIndex ->
                    selectedItemIndex = newIndex
                }
            }
        }
    }
}

@Composable
private fun FoodRow(
    selectedItemIndex: Int?,
    index: Int,
    foodItem: FoodItem,
    onItemClick: (Int) -> Unit // Callback to update selectedItemIndex
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(index) } // Call the callback on click
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

