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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FoodPageContent(
    foodPage: FoodPage,
    selectedItemIndex: Int?, // Selected row index for highlighting
    onItemSelected: (Int) -> Unit // Callback when an item is clicked
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = foodPage.pageTitle,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 80.dp), // reserve space for overlaid PageIndicator
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        FoodHeader()
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            foodPage.data.forEachIndexed { index, foodItem ->
                FoodRow(
                    selectedItemIndex = selectedItemIndex,
                    index = index,
                    foodItem = foodItem,
                    onItemClick = onItemSelected
                )
            }
        }
    }
}

@Composable
fun PageIndicator(currentPage: Int, totalPages: Int, modifier: Modifier = Modifier) {
    Text(
        text = "${currentPage + 1}/$totalPages",
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
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

@Composable
private fun FoodHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "Nimi", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
        Text(text = "Määrä", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(text = "Massa", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(text = "Hiilihydr.", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
    }
}
