package com.therxmv.churros

import androidx.compose.runtime.Composable
import com.therxmv.churros.core.design.ChurrosTheme
import com.therxmv.churros.core.navigation.AppNavGraph

@Composable
fun App() {
    ChurrosTheme {
        AppNavGraph()
    }
}
