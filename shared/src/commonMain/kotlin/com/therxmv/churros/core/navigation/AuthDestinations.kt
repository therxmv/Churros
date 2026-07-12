package com.therxmv.churros.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object SplashDestination : NavKey

@Serializable
data object LoginDestination : NavKey

@Serializable
data object RegisterDestination : NavKey
