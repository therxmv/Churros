package com.therxmv.churros.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.PreviewWrapper
import coil3.compose.AsyncImage
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Espresso
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.SemanticEvent
import com.therxmv.churros.core.design.SemanticInfo
import com.therxmv.churros.core.design.SemanticSuccess

private val AvatarColorPool = listOf(
    Honey500,
    SemanticSuccess,
    SemanticInfo,
    SemanticEvent,
    Espresso,
)

private fun avatarColor(displayName: String): Color =
    AvatarColorPool[kotlin.math.abs(displayName.hashCode()) % AvatarColorPool.size]

private fun avatarInitials(displayName: String): String =
    displayName.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .take(2)
        .joinToString("")
        .ifEmpty { "?" }

/**
 * Circular avatar that shows a photo when [avatarUrl] is available, or a themed
 * initials fallback when it is null.
 *
 * Fallback background color is derived deterministically from [displayName], so
 * the same person always gets the same color. Initials text is always white.
 *
 * @param avatarUrl   Remote image URL; null triggers the initials fallback.
 * @param displayName Used for both initials and color derivation.
 * @param size        Diameter of the avatar circle.
 */
@Composable
fun ChurrosAvatar(
    avatarUrl: String?,
    displayName: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    if (avatarUrl != null) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = displayName,
            modifier = modifier
                .size(size)
                .clip(ChurrosShapes.avatar),
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(ChurrosShapes.avatar)
                .background(avatarColor(displayName)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = avatarInitials(displayName),
                color = Color.White,
                fontSize = (size.value * 0.35f).sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun AvatarPreviewContent() {
    val names = listOf("Sarah Johnson", "Leo K", "Anna", "Bob M", "Zara")
    Row(modifier = Modifier.padding(ChurrosSpacing.M)) {
        names.forEach { name ->
            Box(modifier = Modifier.padding(end = ChurrosSpacing.S)) {
                ChurrosAvatar(
                    avatarUrl = null,
                    displayName = name,
                    size = 48.dp,
                )
            }
        }
    }
}
