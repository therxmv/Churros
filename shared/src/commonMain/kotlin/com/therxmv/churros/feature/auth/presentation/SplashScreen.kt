package com.therxmv.churros.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Lightweight auth-check destination. In Phase 1 it immediately navigates to Login
 * with no visible UI. This avoids putting Login directly as the nav root, leaving
 * room for a real session-check in Phase 2.
 */
@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    LaunchedEffect(Unit) {
        onNavigateToLogin()
    }
}
