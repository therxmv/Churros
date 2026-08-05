package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.nav_chores
import churros.shared.generated.resources.nav_family
import churros.shared.generated.resources.nav_home
import com.therxmv.churros.core.design.ChurrosIcons
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
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        BottomNavTab.entries.forEach { tab ->
            val selected = tab.isSelected(currentRoute)
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab.rootRoute) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(tab.label),
                    )
                },
                label = {
                    Text(
                        text = stringResource(tab.label),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}
