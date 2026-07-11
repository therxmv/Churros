package com.therxmv.churros.core.design

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val churrosShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(16.dp),  // buttons
    medium     = RoundedCornerShape(18.dp),  // sticky notes
    large      = RoundedCornerShape(20.dp),  // cards
    extraLarge = RoundedCornerShape(28.dp),  // bottom sheets / dialogs
)

object ChurrosShapes {
    val button      = RoundedCornerShape(16.dp)
    val card        = RoundedCornerShape(20.dp)
    val stickyNote  = RoundedCornerShape(18.dp)
    val bottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val dialog      = RoundedCornerShape(24.dp)
    val avatar      = CircleShape
}

object ChurrosMotion {
    const val durationShort   = 150
    const val durationDefault = 200
    const val durationLong    = 250
}
