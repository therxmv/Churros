package com.therxmv.churros.feature.auth.presentation

sealed interface SplashEffect {
    data object NavigateToHome : SplashEffect
}
