package com.therxmv.churros.feature.notes.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.nav_notes
import churros.shared.generated.resources.placeholder_coming_soon
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${stringResource(Res.string.nav_notes)} — ${stringResource(Res.string.placeholder_coming_soon)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
