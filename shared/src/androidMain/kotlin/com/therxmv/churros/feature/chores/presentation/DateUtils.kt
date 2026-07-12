package com.therxmv.churros.feature.chores.presentation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

internal actual fun todayIsoString(): String {
    return isoFormat.format(Calendar.getInstance().time)
}

internal actual fun weekEndIsoString(): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 6)
    return isoFormat.format(cal.time)
}
