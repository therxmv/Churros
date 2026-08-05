package com.therxmv.churros.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.nav_chores
import churros.shared.generated.resources.nav_family
import churros.shared.generated.resources.nav_home
import com.therxmv.churros.core.design.ChurrosIcons
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.churrosColors
import com.therxmv.churros.core.navigation.AddMemberRoute
import com.therxmv.churros.core.navigation.ChoresRoute
import com.therxmv.churros.core.navigation.FamilyRoute
import com.therxmv.churros.core.navigation.HomeRoute
import com.therxmv.churros.core.navigation.HouseholdProfileRoute
import com.therxmv.churros.core.navigation.ManageFamilyRoute
import com.therxmv.churros.core.navigation.PermissionsRoute
import com.therxmv.churros.core.navigation.ScaffoldRoute
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.KClass

// ── Tab model ─────────────────────────────────────────────────────────────────

private enum class BottomNavTab(
    val label: StringResource,
    val icon: ImageVector,
    val rootRoute: ScaffoldRoute,
    val selectedRoutes: Set<KClass<out ScaffoldRoute>>,
) {
    Home(
        label = Res.string.nav_home,
        icon = ChurrosIcons.Home,
        rootRoute = HomeRoute,
        selectedRoutes = setOf(HomeRoute::class),
    ),
    Chores(
        label = Res.string.nav_chores,
        icon = ChurrosIcons.Check,
        rootRoute = ChoresRoute,
        selectedRoutes = setOf(ChoresRoute::class),
    ),
    Family(
        label = Res.string.nav_family,
        icon = ChurrosIcons.Person,
        rootRoute = FamilyRoute,
        selectedRoutes = setOf(
            FamilyRoute::class,
            ManageFamilyRoute::class,
            AddMemberRoute::class,
            PermissionsRoute::class,
            HouseholdProfileRoute::class,
        ),
    ),
    ;

    fun isSelected(currentRoute: ScaffoldRoute): Boolean = currentRoute::class in selectedRoutes
}

// ── Component ─────────────────────────────────────────────────────────────────

/**
 * Churros bottom navigation bar with three tabs: Home, Chores, and Family.
 *
 * The active tab is determined by [currentRoute]. Family sub-screens (ManageFamily,
 * AddMember, Permissions, HouseholdProfile) keep the Family tab highlighted.
 * Settings and Notifications display no active tab.
 *
 * @param currentRoute  The currently visible scaffold destination.
 * @param onTabSelected Called with the root route for the tapped tab.
 * @param modifier      Applied to the underlying [NavigationBar].
 */
@Composable
fun ChurrosBottomNavBar(
    currentRoute: ScaffoldRoute,
    onTabSelected: (ScaffoldRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.churrosColors.navBarContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            BottomNavTab.entries.forEach { tab ->
                ChurrosNavBarItem(
                    selected = tab.isSelected(currentRoute),
                    icon = tab.icon,
                    label = stringResource(tab.label),
                    onClick = { onTabSelected(tab.rootRoute) },
                )
            }
        }
    }
}

/**
 * A single bottom-nav tab. The selected tab renders as one rounded card wrapping both the
 * icon and the label — matching the design mockups, which is not achievable with Material3's
 * stock [androidx.compose.material3.NavigationBarItem] (its indicator only wraps the icon).
 */
@Composable
private fun ChurrosNavBarItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    val contentColor = if (selected) {
        MaterialTheme.churrosColors.navSelectedContent
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.churrosColors.navSelectedBackground else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun BottomNavBarPreviewContent() {
    Column {
        ChurrosBottomNavBar(currentRoute = HomeRoute, onTabSelected = {})
        ChurrosBottomNavBar(currentRoute = ChoresRoute, onTabSelected = {})
        ChurrosBottomNavBar(currentRoute = FamilyRoute, onTabSelected = {})
    }
}
