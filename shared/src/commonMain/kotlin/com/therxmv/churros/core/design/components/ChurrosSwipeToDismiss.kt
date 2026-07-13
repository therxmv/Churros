package com.therxmv.churros.core.design.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.SemanticError
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.chore_delete_content_desc
import com.therxmv.churros.core.design.ChurrosIcons
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Swipe-to-dismiss wrapper following the Churros design system.
 *
 * Reveals a red delete background when swiped from end (right).
 * Calls [onDismissed] when the item is fully dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurrosSwipeToDismiss(
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            value == SwipeToDismissBoxValue.EndToStart
        },
    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDismissed()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            ChurrosSwipeDismissBackground(dismissState = dismissState)
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChurrosSwipeDismissBackground(
    dismissState: SwipeToDismissBoxState,
) {
    val isSwiping = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart

    val backgroundColor by animateColorAsState(
        targetValue = if (isSwiping) SemanticError else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "swipe_background_color",
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(ChurrosShapes.card)
            .background(backgroundColor)
            .padding(end = ChurrosSpacing.L),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(ChurrosIcons.Delete),
            contentDescription = stringResource(Res.string.chore_delete_content_desc),
            tint = Color.White,
            modifier = Modifier.size(24.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun SwipeToDismissPreviewContent() {
    ChurrosSwipeToDismiss(onDismissed = {}) {
        ChurrosCard {
            androidx.compose.material3.Text("Swipe me to dismiss")
        }
    }
}
