package com.therxmv.churros.core.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

// ── Shared preview content ────────────────────────────────────────────────────

@Composable
private fun ColorSwatch(color: Color, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, RoundedCornerShape(8.dp)),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ThemePreviewContent() {
    val churros = MaterialTheme.churrosColors
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Typography scale
            Text("Display", style = MaterialTheme.typography.displaySmall)
            Text("Headline", style = MaterialTheme.typography.headlineLarge)
            Text("Title Large", style = MaterialTheme.typography.titleLarge)
            Text("Title Medium", style = MaterialTheme.typography.titleMedium)
            Text("Body Large", style = MaterialTheme.typography.bodyLarge)
            Text("Body Medium", style = MaterialTheme.typography.bodyMedium)
            Text("Label", style = MaterialTheme.typography.labelLarge)
            Text("Caption", style = MaterialTheme.typography.labelSmall)

            Spacer(Modifier.height(24.dp))

            // Brand colors
            Text("Brand", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ColorSwatch(Honey500, "Honey 500")
                ColorSwatch(Honey600, "Honey 600")
                ColorSwatch(Honey300, "Honey 300")
                ColorSwatch(Espresso, "Espresso")
            }

            Spacer(Modifier.height(16.dp))

            // Background / surface tokens
            Text("Surfaces", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ColorSwatch(MaterialTheme.colorScheme.background, "Background")
                ColorSwatch(MaterialTheme.colorScheme.surface, "Surface")
                ColorSwatch(MaterialTheme.colorScheme.surfaceVariant, "Variant")
                ColorSwatch(churros.surfaceElevated, "Elevated")
            }

            Spacer(Modifier.height(16.dp))

            // Semantic
            Text("Semantic", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ColorSwatch(churros.success, "Success")
                ColorSwatch(churros.info, "Info")
                ColorSwatch(churros.event, "Event")
                ColorSwatch(churros.warning, "Warning")
                ColorSwatch(churros.error, "Error")
            }

            Spacer(Modifier.height(16.dp))

            // Sticky notes
            Text("Sticky Notes", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ColorSwatch(churros.stickyShoppingBackground, "Shopping")
                ColorSwatch(churros.stickyChoresBackground, "Chores")
                ColorSwatch(churros.stickyReminderBackground, "Reminder")
                ColorSwatch(churros.stickyEventBackground, "Event")
                ColorSwatch(churros.stickyImportantBackground, "Important")
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview
@Composable
fun ChurrosThemeLightPreview() {
    ChurrosTheme(darkTheme = false) {
        ThemePreviewContent()
    }
}

@Preview
@Composable
fun ChurrosThemeDarkPreview() {
    ChurrosTheme(darkTheme = true) {
        ThemePreviewContent()
    }
}
