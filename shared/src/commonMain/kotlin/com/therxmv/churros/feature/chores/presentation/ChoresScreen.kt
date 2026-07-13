package com.therxmv.churros.feature.chores.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.chore_category_cleaning
import churros.shared.generated.resources.chore_category_cooking
import churros.shared.generated.resources.chore_category_laundry
import churros.shared.generated.resources.chore_category_other
import churros.shared.generated.resources.chore_category_shopping
import churros.shared.generated.resources.chore_due_date_label
import churros.shared.generated.resources.chores_action_cancel
import churros.shared.generated.resources.chores_action_save
import churros.shared.generated.resources.chores_add_title
import churros.shared.generated.resources.chores_empty_subtitle
import churros.shared.generated.resources.chores_empty_title
import churros.shared.generated.resources.chores_error_title_required
import churros.shared.generated.resources.chores_fab_add
import churros.shared.generated.resources.chores_field_category
import churros.shared.generated.resources.chores_field_due_date
import churros.shared.generated.resources.chores_field_title
import churros.shared.generated.resources.chores_filter_all
import churros.shared.generated.resources.chores_filter_this_week
import churros.shared.generated.resources.chores_filter_today
import churros.shared.generated.resources.chores_progress_label
import churros.shared.generated.resources.chores_section_this_week
import churros.shared.generated.resources.chores_section_today
import churros.shared.generated.resources.chores_title
import com.therxmv.churros.core.design.ChurrosIcons
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.SemanticError
import com.therxmv.churros.core.design.SemanticEvent
import com.therxmv.churros.core.design.SemanticInfo
import com.therxmv.churros.core.design.SemanticSuccess
import com.therxmv.churros.core.design.SemanticWarning
import com.therxmv.churros.core.design.churrosColors
import com.therxmv.churros.core.design.components.ChurrosCard
import com.therxmv.churros.core.design.components.ChurrosCheckbox
import com.therxmv.churros.core.design.components.ChurrosCircularProgress
import com.therxmv.churros.core.design.components.ChurrosFab
import com.therxmv.churros.core.design.components.ChurrosFilterChip
import com.therxmv.churros.core.design.components.ChurrosPrimaryButton
import com.therxmv.churros.core.design.components.ChurrosSecondaryButton
import com.therxmv.churros.core.design.components.ChurrosSwipeToDismiss
import com.therxmv.churros.core.design.components.ChurrosTextField
import com.therxmv.churros.feature.chores.domain.model.ChoreCategory
import com.therxmv.churros.feature.chores.domain.model.ChoreFilter
import com.therxmv.churros.feature.chores.domain.model.ChoreItem
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChoresScreen(
    viewModel: ChoresViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    androidx.compose.runtime.LaunchedEffect(lifecycleOwner, viewModel.effects) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect {
                // No effects yet — reserved for future use
            }
        }
    }

    ChoresScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        contentPadding = contentPadding,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoresScreenContent(
    state: ChoresState,
    onEvent: (ChoresEvent) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = ChurrosSpacing.pagePadding,
                end = ChurrosSpacing.pagePadding,
                bottom = ChurrosSpacing.XXL + contentPadding.calculateBottomPadding(),
            ),
        ) {
            // ── Title ────────────────────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(ChurrosSpacing.XL))
                Text(
                    text = stringResource(Res.string.chores_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(ChurrosSpacing.L))
            }

            // ── Circular progress indicator ──────────────────────────────────────
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    ChurrosCircularProgress(
                        progress = state.progress,
                        size = 140.dp,
                        strokeWidth = 12.dp,
                        label = stringResource(Res.string.chores_progress_label),
                    )
                }
                Spacer(modifier = Modifier.height(ChurrosSpacing.L))
            }

            // ── Filter chips ─────────────────────────────────────────────────────
            item {
                ChoresFilterRow(
                    activeFilter = state.activeFilter,
                    onFilterSelected = { onEvent(ChoresEvent.FilterSelected(it)) },
                )
                Spacer(modifier = Modifier.height(ChurrosSpacing.M))
            }

            // ── Empty state ──────────────────────────────────────────────────────
            if (state.filteredAndGroupedChores.isEmpty()) {
                item {
                    ChoresEmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ChurrosSpacing.XL),
                    )
                }
            } else {
                state.filteredAndGroupedChores.forEach { section ->

                    item(key = "header_${section.header}") {
                        ChoreSectionHeaderItem(header = section.header)
                        Spacer(modifier = Modifier.height(ChurrosSpacing.S))
                    }

                    items(
                        items = section.chores,
                        key = { it.id },
                    ) { chore ->
                        ChoresListItem(
                            chore = chore,
                            onToggleCompletion = { isCompleted ->
                                onEvent(ChoresEvent.ChoreCompletionToggled(chore.id, isCompleted))
                            },
                            onDelete = { onEvent(ChoresEvent.ChoreDeleted(chore.id)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                        )
                        Spacer(modifier = Modifier.height(ChurrosSpacing.S))
                    }

                    item(key = "spacer_${section.header}") {
                        Spacer(modifier = Modifier.height(ChurrosSpacing.S))
                    }
                }
            }
        }

        // ── FAB ──────────────────────────────────────────────────────────────────
        ChurrosFab(
            onClick = { onEvent(ChoresEvent.AddChoreClicked) },
            icon = ChurrosIcons.ChoresFilled,
            contentDescription = stringResource(Res.string.chores_fab_add),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = ChurrosSpacing.pagePadding,
                    bottom = ChurrosSpacing.XL + contentPadding.calculateBottomPadding(),
                ),
        )
    }

    // ── Add Chore bottom sheet ────────────────────────────────────────────────
    if (state.isAddSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(ChoresEvent.AddChoreSheetDismissed) },
            sheetState = sheetState,
            shape = ChurrosShapes.bottomSheet,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            AddChoreSheetContent(
                state = state,
                onEvent = onEvent,
            )
        }
    }
}

// ── Filter row ────────────────────────────────────────────────────────────────

@Composable
private fun ChoresFilterRow(
    activeFilter: ChoreFilter,
    onFilterSelected: (ChoreFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosFilterChip(
            label = stringResource(Res.string.chores_filter_today),
            selected = activeFilter == ChoreFilter.TODAY,
            onClick = { onFilterSelected(ChoreFilter.TODAY) },
        )
        ChurrosFilterChip(
            label = stringResource(Res.string.chores_filter_this_week),
            selected = activeFilter == ChoreFilter.THIS_WEEK,
            onClick = { onFilterSelected(ChoreFilter.THIS_WEEK) },
        )
        ChurrosFilterChip(
            label = stringResource(Res.string.chores_filter_all),
            selected = activeFilter == ChoreFilter.ALL,
            onClick = { onFilterSelected(ChoreFilter.ALL) },
        )
    }
}

// ── Date section header ───────────────────────────────────────────────────────

@Composable
private fun ChoreSectionHeaderItem(
    header: ChoreSectionHeader,
    modifier: Modifier = Modifier,
) {
    val label = when (header) {
        ChoreSectionHeader.Today -> stringResource(Res.string.chores_section_today)
        ChoreSectionHeader.ThisWeek -> stringResource(Res.string.chores_section_this_week)
        is ChoreSectionHeader.Later -> header.date
    }
    Text(
        text = label,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.churrosColors.textSecondary,
        modifier = modifier,
    )
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun ChoresEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        Text(
            text = stringResource(Res.string.chores_empty_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(Res.string.chores_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.churrosColors.textSecondary,
        )
    }
}

// ── Chore list item ───────────────────────────────────────────────────────────

@Composable
private fun ChoresListItem(
    chore: ChoreItem,
    onToggleCompletion: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChurrosSwipeToDismiss(
        onDismissed = onDelete,
        modifier = modifier,
    ) {
        ChurrosCard(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (chore.isCompleted) 0.6f else 1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
            ) {
                ChurrosCheckbox(
                    checked = chore.isCompleted,
                    onCheckedChange = onToggleCompletion,
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chore.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = if (chore.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        ),
                        color = if (chore.isCompleted) {
                            MaterialTheme.churrosColors.textTertiary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(ChurrosSpacing.XS))

                    Text(
                        text = stringResource(Res.string.chore_due_date_label, chore.dueDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.churrosColors.textSecondary,
                    )
                }

                ChoreCategoryBadge(category = chore.category)
            }
        }
    }
}

// ── Category badge ────────────────────────────────────────────────────────────

@Composable
private fun ChoreCategoryBadge(
    category: ChoreCategory,
    modifier: Modifier = Modifier,
) {
    val (color, icon, label) = categoryMeta(category)

    Row(
        modifier = modifier
            .clip(ChurrosShapes.button)
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = ChurrosSpacing.S, vertical = ChurrosSpacing.XS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.XS),
    ) {
        androidx.compose.material3.Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(label),
            tint = color,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

@Composable
private fun categoryMeta(category: ChoreCategory): Triple<Color, DrawableResource, StringResource> =
    when (category) {
        ChoreCategory.CLEANING -> Triple(SemanticInfo, ChurrosIcons.CategoryCleaning, Res.string.chore_category_cleaning)
        ChoreCategory.COOKING -> Triple(SemanticWarning, ChurrosIcons.CategoryCooking, Res.string.chore_category_cooking)
        ChoreCategory.SHOPPING -> Triple(SemanticSuccess, ChurrosIcons.CategoryShopping, Res.string.chore_category_shopping)
        ChoreCategory.LAUNDRY -> Triple(SemanticEvent, ChurrosIcons.CategoryLaundry, Res.string.chore_category_laundry)
        ChoreCategory.OTHER -> Triple(SemanticError, ChurrosIcons.CategoryOther, Res.string.chore_category_other)
    }

// ── Add Chore bottom sheet ────────────────────────────────────────────────────

@Composable
private fun AddChoreSheetContent(
    state: ChoresState,
    onEvent: (ChoresEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ChurrosSpacing.pagePadding)
            .padding(bottom = ChurrosSpacing.XL),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.M),
    ) {
        Text(
            text = stringResource(Res.string.chores_add_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        ChurrosTextField(
            value = state.addChoreTitle,
            onValueChange = { onEvent(ChoresEvent.AddTitleChanged(it)) },
            label = stringResource(Res.string.chores_field_title),
            errorMessage = state.addChoreTitleError?.let {
                stringResource(Res.string.chores_error_title_required)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        ChurrosTextField(
            value = state.addChoreDueDate,
            onValueChange = { onEvent(ChoresEvent.AddDueDateChanged(it)) },
            label = stringResource(Res.string.chores_field_due_date),
            placeholder = "YYYY-MM-DD",
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = stringResource(Res.string.chores_field_category),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.churrosColors.textSecondary,
        )

        ChoreCategorySelector(
            selectedCategory = state.addChoreCategory,
            onCategorySelected = { onEvent(ChoresEvent.AddCategorySelected(it)) },
        )

        Spacer(modifier = Modifier.height(ChurrosSpacing.XS))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        ) {
            ChurrosSecondaryButton(
                text = stringResource(Res.string.chores_action_cancel),
                onClick = { onEvent(ChoresEvent.AddChoreSheetDismissed) },
                modifier = Modifier.weight(1f),
            )
            ChurrosPrimaryButton(
                text = stringResource(Res.string.chores_action_save),
                onClick = { onEvent(ChoresEvent.SaveChoreClicked) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ChoreCategorySelector(
    selectedCategory: ChoreCategory,
    onCategorySelected: (ChoreCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.XS),
    ) {
        ChoreCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            val (color, icon, label) = categoryMeta(category)
            val containerColor = if (isSelected) color else MaterialTheme.colorScheme.surfaceVariant
            val contentColor = if (isSelected) Color.White else MaterialTheme.churrosColors.textSecondary

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(ChurrosShapes.card)
                    .background(containerColor)
                    .clickable { onCategorySelected(category) }
                    .padding(vertical = ChurrosSpacing.S, horizontal = ChurrosSpacing.XS),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.XS),
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(label),
                    tint = contentColor,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = stringResource(label),
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun ChoresScreenPreview() {
    ChoresScreenContent(
        state = ChoresState(
            chores = listOf(
                ChoreItem(id = "1", title = "Vacuum living room", category = ChoreCategory.CLEANING, dueDate = "2026-07-12"),
                ChoreItem(id = "2", title = "Cook dinner for the whole family", category = ChoreCategory.COOKING, dueDate = "2026-07-12"),
                ChoreItem(id = "3", title = "Buy groceries", category = ChoreCategory.SHOPPING, dueDate = "2026-07-13"),
                ChoreItem(id = "4", title = "Wash laundry", category = ChoreCategory.LAUNDRY, dueDate = "2026-07-14", isCompleted = true),
                ChoreItem(id = "5", title = "Fix kitchen light", category = ChoreCategory.OTHER, dueDate = "2026-07-18"),
            ),
            progress = 0.25f,
            activeFilter = ChoreFilter.ALL,
            filteredAndGroupedChores = listOf(
                ChoreSection(
                    header = ChoreSectionHeader.Today,
                    chores = listOf(
                        ChoreItem(id = "1", title = "Vacuum living room", category = ChoreCategory.CLEANING, dueDate = "2026-07-12"),
                        ChoreItem(id = "2", title = "Cook dinner for the whole family", category = ChoreCategory.COOKING, dueDate = "2026-07-12"),
                    ),
                ),
                ChoreSection(
                    header = ChoreSectionHeader.ThisWeek,
                    chores = listOf(
                        ChoreItem(id = "3", title = "Buy groceries", category = ChoreCategory.SHOPPING, dueDate = "2026-07-13"),
                        ChoreItem(id = "4", title = "Wash laundry", category = ChoreCategory.LAUNDRY, dueDate = "2026-07-14", isCompleted = true),
                    ),
                ),
                ChoreSection(
                    header = ChoreSectionHeader.Later("2026-07-18"),
                    chores = listOf(
                        ChoreItem(id = "5", title = "Fix kitchen light", category = ChoreCategory.OTHER, dueDate = "2026-07-18"),
                    ),
                ),
            ),
        ),
        onEvent = {},
    )
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun ChoresScreenEmptyPreview() {
    ChoresScreenContent(
        state = ChoresState(
            chores = emptyList(),
            filteredAndGroupedChores = emptyList(),
        ),
        onEvent = {},
    )
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun AddChoreSheetPreview() {
    ChoresScreenContent(
        state = ChoresState(
            chores = emptyList(),
            filteredAndGroupedChores = emptyList(),
            isAddSheetVisible = true,
            addChoreCategory = ChoreCategory.CLEANING,
        ),
        onEvent = {},
    )
}
