package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.Espresso
import com.therxmv.churros.core.design.Honey500
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.chores_fab_add
import com.therxmv.churros.core.design.ChurrosIcons

@Composable
fun ChurrosFab(
    onClick: () -> Unit,
    icon: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = ChurrosShapes.button,
        containerColor = Honey500,
        contentColor = Espresso,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun FabPreviewContent() {
    ChurrosFab(
        onClick = {},
        icon = ChurrosIcons.ChoresFilled,
        contentDescription = stringResource(Res.string.chores_fab_add),
    )
}
