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
import com.therxmv.churros.core.design.components.DateTimePickerPreviewContent
import com.therxmv.churros.core.design.components.DividerPreviewContent
import com.therxmv.churros.core.design.components.FilterChipPreviewContent
import com.therxmv.churros.core.design.components.LogoPreviewContent
import com.therxmv.churros.core.design.components.OnboardingPagerPreviewContent
import com.therxmv.churros.core.design.components.OtpFieldPreviewContent
import com.therxmv.churros.core.design.components.ProgressRingPreviewContent
import com.therxmv.churros.core.design.components.RoleBadgePreviewContent
import com.therxmv.churros.core.design.components.TextFieldPreviewContent
import com.therxmv.churros.core.design.components.ToggleRowPreviewContent
import com.therxmv.churros.feature.chores.presentation.components.CategoryPickerPreviewContent
import com.therxmv.churros.feature.chores.presentation.components.PrioritySelectorPreviewContent
import com.therxmv.churros.feature.chores.presentation.components.RepeatSelectorPreviewContent
import com.therxmv.churros.feature.family.presentation.components.MemberCardPreviewContent
import com.therxmv.churros.feature.notifications.presentation.components.NotificationItemActionablePreview
import com.therxmv.churros.feature.notifications.presentation.components.NotificationItemInfoPreview
import com.therxmv.churros.feature.auth.presentation.SplashScreenPreview
import com.therxmv.churros.feature.auth.presentation.login.LoginScreenErrorPreview
import com.therxmv.churros.feature.auth.presentation.login.LoginScreenPreview
import com.therxmv.churros.feature.auth.presentation.register.RegisterScreenErrorPreview
import com.therxmv.churros.feature.auth.presentation.register.RegisterScreenPreview
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
fun DividerScreenshot() = DividerPreviewContent()

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

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ToggleRowScreenshot() = ToggleRowPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun FilterChipScreenshot() = FilterChipPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ProgressRingScreenshot() = ProgressRingPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun RoleBadgeScreenshot() = RoleBadgePreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OtpFieldScreenshot() = OtpFieldPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingPagerScreenshot() = OnboardingPagerPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun DateTimePickerScreenshot() = DateTimePickerPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun CategoryPickerScreenshot() = CategoryPickerPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun RepeatSelectorScreenshot() = RepeatSelectorPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun PrioritySelectorScreenshot() = PrioritySelectorPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun MemberCardScreenshot() = MemberCardPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun NotificationItemActionableScreenshot() = NotificationItemActionablePreview()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun NotificationItemInfoScreenshot() = NotificationItemInfoPreview()

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
