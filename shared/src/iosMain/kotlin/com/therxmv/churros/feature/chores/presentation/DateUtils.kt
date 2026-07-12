package com.therxmv.churros.feature.chores.presentation

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateByAddingComponents

private val isoFormatter: NSDateFormatter by lazy {
    NSDateFormatter().apply { dateFormat = "yyyy-MM-dd" }
}

internal actual fun todayIsoString(): String {
    return isoFormatter.stringFromDate(NSDate())
}

internal actual fun weekEndIsoString(): String {
    val cal = NSCalendar.currentCalendar
    val comps = NSDateComponents().apply { day = 6 }
    val future = cal.dateByAddingComponents(comps, NSDate(), 0u) ?: NSDate()
    return isoFormatter.stringFromDate(future)
}
