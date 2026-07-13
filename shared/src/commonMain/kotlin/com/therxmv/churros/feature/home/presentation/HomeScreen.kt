package com.therxmv.churros.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.home_greeting_afternoon
import churros.shared.generated.resources.home_greeting_evening
import churros.shared.generated.resources.home_greeting_morning
import churros.shared.generated.resources.home_progress_chores
import churros.shared.generated.resources.home_progress_title
import churros.shared.generated.resources.home_tasks_empty
import churros.shared.generated.resources.home_tasks_title
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.churrosColors
import com.therxmv.churros.core.design.components.ChurrosBottomNavBar
import com.therxmv.churros.core.design.components.ChurrosCard
import com.therxmv.churros.core.design.components.ChurrosCheckboxRow
import com.therxmv.churros.core.design.components.HomeTab
import com.therxmv.churros.feature.chores.presentation.ChoresScreen
import com.therxmv.churros.feature.home.domain.model.TaskItem
import com.therxmv.churros.feature.notes.presentation.NotesScreen
import com.therxmv.churros.feature.shopping.presentation.ShoppingScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    androidx.compose.runtime.LaunchedEffect(lifecycleOwner, viewModel.effects) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect { effect ->
                // No effects yet — reserved for future use
            }
        }
    }

    HomeScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun HomeScreenContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.HOME) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ChurrosBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        },
    ) { paddingValues ->
        when (selectedTab) {
            HomeTab.HOME -> HomeTabContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.padding(paddingValues),
            )
            HomeTab.CHORES -> ChoresScreen()
            HomeTab.SHOPPING -> ShoppingScreen()
            HomeTab.NOTES -> NotesScreen()
        }
    }
}

@Composable
private fun HomeTabContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ChurrosSpacing.pagePadding)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(ChurrosSpacing.XL))

        HomeGreeting(userName = state.userName)

        Spacer(modifier = Modifier.height(ChurrosSpacing.L))

        HomeProgressCard(
            completedCount = state.completedCount,
            totalCount = state.totalCount,
        )

        Spacer(modifier = Modifier.height(ChurrosSpacing.M))

        HomeTodayTasksCard(
            tasks = state.tasks,
            onCheckedChange = { taskId, isChecked ->
                onEvent(HomeEvent.TaskCheckedChanged(taskId, isChecked))
            },
        )

        Spacer(modifier = Modifier.height(ChurrosSpacing.XL))
    }
}

@Composable
private fun HomeGreeting(
    userName: String,
    modifier: Modifier = Modifier,
) {
    val greetingLabel = greetingForCurrentHour()
    Column(modifier = modifier) {
        Text(
            text = stringResource(greetingLabel),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.churrosColors.textSecondary,
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun greetingForCurrentHour() = run {
    // Use a simple fallback — platform clock can be injected later
    Res.string.home_greeting_morning
}

@Composable
private fun HomeProgressCard(
    completedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    ChurrosCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(Res.string.home_progress_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(Res.string.home_progress_chores, completedCount, totalCount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.churrosColors.textSecondary,
            )
        }

        Spacer(modifier = Modifier.height(ChurrosSpacing.S))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = Honey500,
            trackColor = MaterialTheme.colorScheme.outline,
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
private fun HomeTodayTasksCard(
    tasks: List<TaskItem>,
    onCheckedChange: (taskId: String, isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ChurrosCard(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.home_tasks_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(ChurrosSpacing.S))

        if (tasks.isEmpty()) {
            Text(
                text = stringResource(Res.string.home_tasks_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.churrosColors.textSecondary,
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.XS)) {
                tasks.forEach { task ->
                    ChurrosCheckboxRow(
                        label = task.title,
                        checked = task.isChecked,
                        onCheckedChange = { isChecked -> onCheckedChange(task.id, isChecked) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        state = HomeState(
            userName = "Roman",
            tasks = listOf(
                TaskItem(id = "1", title = "Take out trash", isChecked = true),
                TaskItem(id = "2", title = "Vacuum living room"),
                TaskItem(id = "3", title = "Wash dishes"),
                TaskItem(id = "4", title = "Feed the cat"),
            ),
            completedCount = 1,
            totalCount = 4,
        ),
        onEvent = {},
    )
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun HomeScreenEmptyPreview() {
    HomeScreenContent(
        state = HomeState(
            userName = "Roman",
            tasks = emptyList(),
            completedCount = 0,
            totalCount = 0,
        ),
        onEvent = {},
    )
}
