package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.role_badge_caregiver
import churros.shared.generated.resources.role_badge_kid
import churros.shared.generated.resources.role_badge_parent
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors
import com.therxmv.churros.feature.family.domain.model.HouseholdRole
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Role types available in the UI layer.
 *
 * Extends [HouseholdRole] (PARENT / KID) with CAREGIVER, which appears in the
 * Add Member dialog but has not yet been promoted to the domain model.
 */
enum class ChurrosRoleType {
    PARENT, KID, CAREGIVER;

    val labelRes: StringResource
        get() = when (this) {
            PARENT -> Res.string.role_badge_parent
            KID -> Res.string.role_badge_kid
            CAREGIVER -> Res.string.role_badge_caregiver
        }
}

/** Maps a domain [HouseholdRole] to its UI [ChurrosRoleType]. */
fun HouseholdRole.toChurrosRoleType(): ChurrosRoleType = when (this) {
    HouseholdRole.PARENT -> ChurrosRoleType.PARENT
    HouseholdRole.KID -> ChurrosRoleType.KID
}

/**
 * Compact label chip displaying a household role (Parent / Kid / Caregiver).
 *
 * Colors are driven by [ChurrosRoleType]:
 * - Parent → Espresso-based container from [churrosColors.roleParent]
 * - Kid    → Green-based container from [churrosColors.roleKid]
 * - Caregiver → onSurfaceVariant on surfaceVariant
 */
@Composable
fun ChurrosRoleBadge(
    role: ChurrosRoleType,
    modifier: Modifier = Modifier,
) {
    val (container, content) = roleBadgeColors(role)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = container,
    ) {
        Text(
            text = stringResource(role.labelRes),
            style = MaterialTheme.typography.labelLarge,
            color = content,
            modifier = Modifier.padding(
                horizontal = ChurrosSpacing.S,
                vertical = ChurrosSpacing.XS,
            ),
        )
    }
}

@Composable
private fun roleBadgeColors(role: ChurrosRoleType): Pair<Color, Color> {
    val colors = MaterialTheme.churrosColors
    return when (role) {
        ChurrosRoleType.PARENT -> Pair(
            colors.roleParent.copy(alpha = 0.15f),
            colors.roleParent,
        )
        ChurrosRoleType.KID -> Pair(
            colors.roleKid.copy(alpha = 0.15f),
            colors.roleKid,
        )
        ChurrosRoleType.CAREGIVER -> Pair(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun RoleBadgePreviewContent() {
    Row(
        modifier = Modifier.padding(ChurrosSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChurrosRoleBadge(role = ChurrosRoleType.PARENT)
        ChurrosRoleBadge(role = ChurrosRoleType.KID)
        ChurrosRoleBadge(role = ChurrosRoleType.CAREGIVER)
    }
}
