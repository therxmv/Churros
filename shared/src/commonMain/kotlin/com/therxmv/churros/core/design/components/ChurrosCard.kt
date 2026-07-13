package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing

@Composable
fun ChurrosCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = ChurrosShapes.card,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(ChurrosSpacing.cardPadding),
            content = content,
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun CardPreviewContent() {
    Column(
        modifier = Modifier.padding(ChurrosSpacing.M),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosCard {
            Text("Card title", style = MaterialTheme.typography.titleMedium)
            Text("Card body text goes here.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
