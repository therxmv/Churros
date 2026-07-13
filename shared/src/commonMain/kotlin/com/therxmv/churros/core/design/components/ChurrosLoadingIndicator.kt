package com.therxmv.churros.core.design.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.therxmv.churros.core.design.Honey500

@Composable
fun ChurrosLoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = Honey500,
    )
}
