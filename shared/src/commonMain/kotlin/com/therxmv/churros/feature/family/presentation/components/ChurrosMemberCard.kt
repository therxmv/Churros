package com.therxmv.churros.feature.family.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.member_tasks_completed
import kotlinx.datetime.Instant
import coil3.compose.AsyncImage
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors
import com.therxmv.churros.core.design.components.ChurrosRoleBadge
import com.therxmv.churros.core.design.components.ChurrosRoleType
import com.therxmv.churros.feature.family.domain.model.HouseholdRole
import com.therxmv.churros.feature.family.domain.model.MemberProfile
import org.jetbrains.compose.resources.stringResource

private val AvatarSize = 48.dp

/**
 * Household member row showing avatar, display name, role badge, and task progress.
 *
 * @param member          Domain [MemberProfile] to render.
 * @param completedChores Number of completed chores for this member.
 * @param totalChores     Total assigned chores for this member.
 */
@Composable
fun ChurrosMemberCard(
    member: MemberProfile,
    completedChores: Int,
    totalChores: Int,
    modifier: Modifier = Modifier,
) {
    val progress = if (totalChores > 0) completedChores.toFloat() / totalChores else 0f
    val roleType = member.householdRole.toChurrosRoleType()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = member.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(AvatarSize)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.width(ChurrosSpacing.M))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
            ) {
                Text(
                    text = member.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                ChurrosRoleBadge(role = roleType)
            }
            Text(
                text = stringResource(
                    Res.string.member_tasks_completed,
                    completedChores,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.churrosColors.textSecondary,
                modifier = Modifier.padding(top = ChurrosSpacing.XS),
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ChurrosSpacing.XS)
                    .clip(ChurrosShapes.button),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

// Convenience mapper lives alongside domain model awareness
private fun HouseholdRole.toChurrosRoleType(): ChurrosRoleType = when (this) {
    HouseholdRole.PARENT -> ChurrosRoleType.PARENT
    HouseholdRole.KID -> ChurrosRoleType.KID
}

// ── Previews ────────────────────────────────────────────────────────────────────

@ChurrosPreview
@Composable
fun MemberCardPreviewContent() {
    val sampleMembers = listOf(
        Triple(
            MemberProfile(
                id = "1",
                displayName = "Sarah",
                avatarUrl = null,
                householdId = "hh1",
                householdRole = HouseholdRole.PARENT,
                joinedAt = Instant.fromEpochSeconds(0),
            ),
            5, 5,
        ),
        Triple(
            MemberProfile(
                id = "2",
                displayName = "Leo",
                avatarUrl = null,
                householdId = "hh1",
                householdRole = HouseholdRole.KID,
                joinedAt = Instant.fromEpochSeconds(0),
            ),
            12, 15,
        ),
    )
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        sampleMembers.forEach { (member, completed, total) ->
            ChurrosMemberCard(
                member = member,
                completedChores = completed,
                totalChores = total,
            )
        }
    }
}
