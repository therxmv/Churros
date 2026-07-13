package com.therxmv.churros

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.android.tools.screenshot.PreviewTest
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ThemePreviewContent
import com.therxmv.churros.core.design.components.BottomNavBarPreviewContent
import com.therxmv.churros.core.design.components.ButtonsPreviewContent
import com.therxmv.churros.core.design.components.CardPreviewContent
import com.therxmv.churros.core.design.components.CheckboxPreviewContent
import com.therxmv.churros.core.design.components.LogoPreviewContent
import com.therxmv.churros.core.design.components.TextFieldPreviewContent
import com.therxmv.churros.feature.auth.presentation.SplashScreenPreview
import com.therxmv.churros.feature.auth.presentation.login.LoginScreenErrorPreview
import com.therxmv.churros.feature.auth.presentation.login.LoginScreenPreview
import com.therxmv.churros.feature.auth.presentation.register.RegisterScreenErrorPreview
import com.therxmv.churros.feature.auth.presentation.register.RegisterScreenPreview
import com.therxmv.churros.feature.home.presentation.HomeScreenEmptyPreview
import com.therxmv.churros.feature.home.presentation.HomeScreenPreview

// ── Components ────────────────────────────────────────────────────────────────

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ButtonsScreenshot() = ButtonsPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun TextFieldScreenshot() = TextFieldPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun CheckboxScreenshot() = CheckboxPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun CardScreenshot() = CardPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LogoScreenshot() = LogoPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun BottomNavBarScreenshot() = BottomNavBarPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ThemeScreenshot() = ThemePreviewContent()

// ── Screens ───────────────────────────────────────────────────────────────────

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun SplashScreenScreenshot() = SplashScreenPreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun HomeScreenScreenshot() = HomeScreenPreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun HomeScreenEmptyScreenshot() = HomeScreenEmptyPreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LoginScreenScreenshot() = LoginScreenPreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LoginScreenErrorScreenshot() = LoginScreenErrorPreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun RegisterScreenScreenshot() = RegisterScreenPreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun RegisterScreenErrorScreenshot() = RegisterScreenErrorPreview()
