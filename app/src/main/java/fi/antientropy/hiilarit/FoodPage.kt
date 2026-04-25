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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fi.antientropy.hiilarit.ui.theme.PaperCream
import fi.antientropy.hiilarit.ui.theme.PaperHighlight
import fi.antientropy.hiilarit.ui.theme.PaperInk
import fi.antientropy.hiilarit.ui.theme.PaperRule

@Composable
fun FoodPageContent(
    foodPage: FoodPage,
    selectedItemIndex: Int?, // Selected row index for highlighting
    onItemSelected: (Int) -> Unit // Callback when an item is clicked
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        color = PaperCream,
        contentColor = PaperInk,
        shape = RoundedCornerShape(2.dp),
        shadowElevation = 6.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = foodPage.pageTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 80.dp), // reserve space for overlaid PageIndicator
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

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
}

@Composable
fun PageIndicator(currentPage: Int, totalPages: Int, modifier: Modifier = Modifier) {
    Text(
        text = "${currentPage + 1}/$totalPages",
        style = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = FontFamily.Serif,
            color = PaperInk
        ),
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
    val rowStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Serif)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(index) }
            .background(
                if (selectedItemIndex == index) PaperHighlight else Color.Transparent
            )
            .padding(vertical = 6.dp)
    ) {
        Text(text = foodItem.nimi, modifier = Modifier.weight(2f), style = rowStyle)
        Text(text = foodItem.maara, modifier = Modifier.weight(1f), style = rowStyle)
        Text(text = foodItem.massa, modifier = Modifier.weight(1f), style = rowStyle)
        Text(text = foodItem.hiilihydraatit, modifier = Modifier.weight(1f), style = rowStyle)
    }
    // Ruled-paper divider line between rows
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(PaperRule.copy(alpha = 0.5f))
    )
}

@Composable
private fun FoodHeader() {
    val headerStyle = MaterialTheme.typography.bodyLarge.copy(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold
    )
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
        Text(text = "Nimi", modifier = Modifier.weight(2f), style = headerStyle)
        Text(text = "Määrä", modifier = Modifier.weight(1f), style = headerStyle)
        Text(text = "Massa", modifier = Modifier.weight(1f), style = headerStyle)
        Text(text = "Hiilihydr.", modifier = Modifier.weight(1f), style = headerStyle)
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(PaperInk.copy(alpha = 0.7f))
    )
}
