package com.therxmv.churros

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.therxmv.churros.core.navigation.AppNavGraph

@Composable
fun App() {
    MaterialTheme {
        AppNavGraph()
    }
}
