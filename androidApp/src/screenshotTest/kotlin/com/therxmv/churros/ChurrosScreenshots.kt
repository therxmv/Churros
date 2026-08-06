package com.therxmv.churros

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.android.tools.screenshot.PreviewTest
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ThemePreviewContent
import com.therxmv.churros.core.design.components.BottomNavBarPreviewContent
import com.therxmv.churros.core.design.components.ButtonPreviewContent
import com.therxmv.churros.core.design.components.CardPreviewContent
import com.therxmv.churros.core.design.components.DateTimePickerPreviewContent
import com.therxmv.churros.core.design.components.FilterChipPreviewContent
import com.therxmv.churros.core.design.components.FullScreenPreviewContent
import com.therxmv.churros.core.design.components.LoadingIndicatorPreviewContent
import com.therxmv.churros.core.design.components.OnboardingPagerPreviewContent
import com.therxmv.churros.core.design.components.OtpFieldPreviewContent
import com.therxmv.churros.core.design.components.ProgressRingPreviewContent
import com.therxmv.churros.core.design.components.RoleBadgePreviewContent
import com.therxmv.churros.core.design.components.ScaffoldScreenPreviewContent
import com.therxmv.churros.core.design.components.ToggleRowPreviewContent
import com.therxmv.churros.feature.chores.presentation.components.CategoryPickerPreviewContent
import com.therxmv.churros.feature.onboarding.presentation.OnboardingSlide1PreviewContent
import com.therxmv.churros.feature.onboarding.presentation.OnboardingSlide2PreviewContent
import com.therxmv.churros.feature.onboarding.presentation.OnboardingSlide3PreviewContent
import com.therxmv.churros.feature.chores.presentation.components.PrioritySelectorPreviewContent
import com.therxmv.churros.feature.chores.presentation.components.RepeatSelectorPreviewContent
import com.therxmv.churros.feature.family.presentation.components.MemberCardPreviewContent
import com.therxmv.churros.feature.notifications.presentation.components.NotificationItemActionablePreview
import com.therxmv.churros.feature.notifications.presentation.components.NotificationItemInfoPreview

// ── Design System ─────────────────────────────────────────────────────────────

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ThemeScreenshot() = ThemePreviewContent()

// ── Components ────────────────────────────────────────────────────────────────

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun CardScreenshot() = CardPreviewContent()

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
fun ButtonScreenshot() = ButtonPreviewContent()

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
fun BottomNavBarScreenshot() = BottomNavBarPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun FullScreenScreenshot() = FullScreenPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LoadingIndicatorScreenshot() = LoadingIndicatorPreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ScaffoldScreenScreenshot() = ScaffoldScreenPreviewContent()

// ── Feature: Onboarding ───────────────────────────────────────────────────────

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingSlide1Screenshot() = OnboardingSlide1PreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingSlide2Screenshot() = OnboardingSlide2PreviewContent()

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingSlide3Screenshot() = OnboardingSlide3PreviewContent()

// ── Feature: Chores ───────────────────────────────────────────────────────────

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

// ── Feature: Family ───────────────────────────────────────────────────────────

@PreviewTest
@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun MemberCardScreenshot() = MemberCardPreviewContent()

// ── Feature: Notifications ────────────────────────────────────────────────────

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
