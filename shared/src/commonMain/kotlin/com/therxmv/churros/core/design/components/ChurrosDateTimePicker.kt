package com.therxmv.churros.core.design.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.chores_field_due_date
import churros.shared.generated.resources.date_picker_confirm
import churros.shared.generated.resources.date_picker_dismiss
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors
import org.jetbrains.compose.resources.stringResource

/**
 * Wrapper around the Material3 system [DatePicker] dialog with Churros styling.
 *
 * Renders a tappable field showing [dateLabel]. Tapping opens the date picker dialog.
 *
 * @param dateLabel          Display string for the currently selected date (or placeholder).
 * @param onDateSelected     Called with the selected epoch-millisecond timestamp.
 * @param modifier           Applied to the tappable field surface.
 * @param initialDateMillis  Pre-selected date epoch millis, or null for today.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurrosDatePicker(
    dateLabel: String,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    initialDateMillis: Long? = null,
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
    )

    ChurrosPickerField(
        label = dateLabel,
        trailingLabel = "📅",
        modifier = modifier.clickable { showDialog = true },
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onDateSelected)
                        showDialog = false
                    },
                ) {
                    Text(stringResource(Res.string.date_picker_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(Res.string.date_picker_dismiss))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * Wrapper around the Material3 [TimePicker] dialog with Churros styling.
 *
 * Renders a tappable field showing [timeLabel]. Tapping opens the time picker dialog.
 *
 * @param timeLabel      Display string for the currently selected time.
 * @param onTimeSelected Called with the selected (hour, minute) pair.
 * @param initialHour    Pre-selected hour (24h, 0–23).
 * @param initialMinute  Pre-selected minute (0–59).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurrosTimePicker(
    timeLabel: String,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    initialHour: Int = 0,
    initialMinute: Int = 0,
) {
    var showDialog by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
    )

    ChurrosPickerField(
        label = timeLabel,
        trailingLabel = "🕐",
        modifier = modifier.clickable { showDialog = true },
    )

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = ChurrosShapes.dialog,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier.padding(ChurrosSpacing.L),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(onClick = { showDialog = false }) {
                            Text(stringResource(Res.string.date_picker_dismiss))
                        }
                        TextButton(
                            onClick = {
                                onTimeSelected(timePickerState.hour, timePickerState.minute)
                                showDialog = false
                            },
                        ) {
                            Text(stringResource(Res.string.date_picker_confirm))
                        }
                    }
                }
            }
        }
    }
}

/** Internal shared field surface used by both date and time pickers. */
@Composable
private fun ChurrosPickerField(
    label: String,
    trailingLabel: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = ChurrosShapes.card,
        color = MaterialTheme.churrosColors.inputBackground,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ChurrosSpacing.M, vertical = ChurrosSpacing.S),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = trailingLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.churrosColors.textSecondary,
            )
        }
    }
}

@ChurrosPreview
@Composable
fun DateTimePickerPreviewContent() {
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosDatePicker(
            dateLabel = stringResource(Res.string.chores_field_due_date),
            onDateSelected = {},
        )
        ChurrosTimePicker(
            timeLabel = "6:00 PM",
            onTimeSelected = { _, _ -> },
            modifier = Modifier.padding(top = ChurrosSpacing.S),
        )
    }
}
