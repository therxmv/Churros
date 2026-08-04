package com.therxmv.churros.feature.notifications.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.notification_action_approve
import churros.shared.generated.resources.notification_action_approve_desc
import churros.shared.generated.resources.notification_action_decline
import churros.shared.generated.resources.notification_action_decline_desc
import coil3.compose.AsyncImage
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors
import org.jetbrains.compose.resources.stringResource

private val AvatarSize = 44.dp

/**
 * Notification list item with two variants:
 *
 * - **Actionable** (`onApprove != null`): renders Approve / Decline buttons below the message.
 *   Used for [com.therxmv.churros.feature.notifications.domain.model.NotificationType.REWARD_REQUEST].
 * - **Informational** (`onApprove == null`): message + timestamp only.
 *
 * @param avatarUrl   URL for the sender's avatar. Null renders a placeholder.
 * @param message     Pre-formatted, possibly annotated notification text.
 * @param timestamp   Human-readable relative time string (e.g. "2 mins ago").
 * @param onApprove   Approve action callback; non-null enables the actionable variant.
 * @param onDecline   Decline action callback (used alongside [onApprove]).
 */
@Composable
fun ChurrosNotificationItem(
    avatarUrl: String?,
    message: String,
    timestamp: String,
    modifier: Modifier = Modifier,
    onApprove: (() -> Unit)? = null,
    onDecline: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ChurrosSpacing.S),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.M),
    ) {
        NotificationAvatar(avatarUrl = avatarUrl)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.churrosColors.textSecondary,
                modifier = Modifier.padding(top = ChurrosSpacing.XS),
            )
            if (onApprove != null) {
                NotificationActions(
                    onApprove = onApprove,
                    onDecline = onDecline ?: {},
                    modifier = Modifier.padding(top = ChurrosSpacing.S),
                )
            }
        }
    }
}

@Composable
private fun NotificationAvatar(avatarUrl: String?) {
    AsyncImage(
        model = avatarUrl,
        contentDescription = null,
        modifier = Modifier
            .size(AvatarSize)
            .clip(CircleShape),
    )
}

@Composable
private fun NotificationActions(
    onApprove: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        Button(
            onClick = onApprove,
            shape = ChurrosShapes.button,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(
                text = stringResource(Res.string.notification_action_approve),
                style = MaterialTheme.typography.labelLarge,
            )
        }
        OutlinedButton(
            onClick = onDecline,
            shape = ChurrosShapes.button,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) {
            Text(
                text = stringResource(Res.string.notification_action_decline),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

// ── Previews ────────────────────────────────────────────────────────────────────

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun NotificationItemActionablePreview() {
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosNotificationItem(
            avatarUrl = null,
            message = "Jamie requested 'Extra Gaming Hour' for 50 pts.",
            timestamp = "2 mins ago",
            onApprove = {},
            onDecline = {},
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun NotificationItemInfoPreview() {
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosNotificationItem(
            avatarUrl = null,
            message = "Mom assigned 'Vacuum Living Room' to you.",
            timestamp = "15 mins ago",
        )
        ChurrosNotificationItem(
            avatarUrl = null,
            message = "Sarah completed 'Feed the Dog'.",
            timestamp = "3 hours ago",
            modifier = Modifier.padding(top = ChurrosSpacing.S),
        )
    }
}
