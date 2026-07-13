package com.therxmv.churros.core.design.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.nav_chores
import churros.shared.generated.resources.nav_home
import churros.shared.generated.resources.nav_notes
import churros.shared.generated.resources.nav_shopping
import com.therxmv.churros.core.design.ChurrosIcons
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.churrosColors
import org.jetbrains.compose.resources.stringResource

enum class HomeTab {
    HOME, CHORES, SHOPPING, NOTES
}

private data class NavBarItem(
    val tab: HomeTab,
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
)

@Composable
fun ChurrosBottomNavBar(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        NavBarItem(
            tab = HomeTab.HOME,
            label = stringResource(Res.string.nav_home),
            selectedIcon = ChurrosIcons.HomeFilled,
            unselectedIcon = ChurrosIcons.HomeOutlined,
        ),
        NavBarItem(
            tab = HomeTab.CHORES,
            label = stringResource(Res.string.nav_chores),
            selectedIcon = ChurrosIcons.ChoresFilled,
            unselectedIcon = ChurrosIcons.ChoresOutlined,
        ),
        NavBarItem(
            tab = HomeTab.SHOPPING,
            label = stringResource(Res.string.nav_shopping),
            selectedIcon = ChurrosIcons.ShoppingFilled,
            unselectedIcon = ChurrosIcons.ShoppingOutlined,
        ),
        NavBarItem(
            tab = HomeTab.NOTES,
            label = stringResource(Res.string.nav_notes),
            selectedIcon = ChurrosIcons.NotesFilled,
            unselectedIcon = ChurrosIcons.NotesOutlined,
        ),
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
    ) {
        items.forEach { item ->
            val isSelected = item.tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        painter = painterResource(if (isSelected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = Honey500,
                    indicatorColor = Honey500,
                    unselectedIconColor = MaterialTheme.churrosColors.textSecondary,
                    unselectedTextColor = MaterialTheme.churrosColors.textSecondary,
                ),
            )
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun BottomNavBarPreviewContent() {
    ChurrosBottomNavBar(
        selectedTab = HomeTab.HOME,
        onTabSelected = {},
    )
}
