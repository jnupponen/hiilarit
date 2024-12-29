package fi.antientropy.hiilarit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodPageTitleAccordion(
    foodData: FoodData,
    onPageSelected: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val accordionPadding = Modifier.padding(12.dp)
        Text(
            modifier = accordionPadding,
            text = if (isExpanded) "Piilota ruokaryhmät..." else "Näytä ruokaryhmät...",
            style = MaterialTheme.typography.titleLarge,
        )
        val icon =
            if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
        Icon(
            modifier = accordionPadding,
            imageVector = icon,
            contentDescription = "Icons.Default.KeyboardArrowUp or Icons.Default.KeyboardArrowDown"
        )
    }

    // Content visible only if accordion is expanded
    if (isExpanded) {
        // Wrap FlowRow in a Column with vertical scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState) // Enable vertical scrolling
                .padding(16.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                foodData.pages.forEachIndexed { index, page ->
                    FoodPageTitleTag(page.pageTitle) {
                        onPageSelected(index)
                        isExpanded = false
                    }
                }
            }
        }
    }
}

@Composable
fun FoodPageTitleTag(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
