package com.therxmv.churros.feature.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.action_create_account
import churros.shared.generated.resources.action_sign_in
import churros.shared.generated.resources.ic_app_logo
import churros.shared.generated.resources.onboarding_cta_next
import churros.shared.generated.resources.onboarding_cta_skip
import churros.shared.generated.resources.onboarding_slide1_body
import churros.shared.generated.resources.onboarding_slide1_title
import churros.shared.generated.resources.onboarding_slide2_body
import churros.shared.generated.resources.onboarding_slide2_title
import churros.shared.generated.resources.onboarding_slide3_body
import churros.shared.generated.resources.onboarding_slide3_title
import com.therxmv.churros.core.design.ChurrosIcons
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.components.ChurrosButton
import com.therxmv.churros.core.design.components.ChurrosOnboardingPager
import com.therxmv.churros.core.design.components.ChurrosOutlinedButton
import com.therxmv.churros.core.design.components.ChurrosTextButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Onboarding carousel screen — shown once on first app launch.
 *
 * Renders 3 horizontally-swipeable slides via [ChurrosOnboardingPager]:
 * 1. "Welcome to Churros" — shared-home value prop
 * 2. "Share chores, not the stress" — task-tracking value prop
 * 3. "Everything your home needs" — family-space value prop + CTAs
 *
 * Navigation callbacks are provided by the nav graph so the composable
 * stays decoupled from the back-stack. [OnboardingViewModel] marks the
 * first-launch flag before any navigation takes place.
 *
 * @param onNavigateToSignIn  Called after the flag is persisted; the nav graph
 *   should clear the back-stack and push [FullscreenRoute.SignInRoute].
 * @param onNavigateToSignUp  Called after the flag is persisted; the nav graph
 *   should clear the back-stack and push [FullscreenRoute.SignUpRoute].
 * @param modifier Applied to the root [Box].
 */
@Composable
fun OnboardingScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner, viewModel.effects) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    OnboardingUiEffect.NavigateToSignIn -> onNavigateToSignIn()
                    OnboardingUiEffect.NavigateToSignUp -> onNavigateToSignUp()
                }
            }
        }
    }

    val pagerState = rememberPagerState(pageCount = { ONBOARDING_PAGE_COUNT })
    val scope = rememberCoroutineScope()

    val pages: List<@Composable () -> Unit> = listOf(
        {
            OnboardingSlideContent(
                title = stringResource(Res.string.onboarding_slide1_title),
                body = stringResource(Res.string.onboarding_slide1_body),
            )
        },
        {
            OnboardingSlideContent(
                title = stringResource(Res.string.onboarding_slide2_title),
                body = stringResource(Res.string.onboarding_slide2_body),
            )
        },
        {
            OnboardingSlideContent(
                title = stringResource(Res.string.onboarding_slide3_title),
                body = stringResource(Res.string.onboarding_slide3_body),
            )
        },
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ChurrosOnboardingPager(
                pages = pages,
                pagerState = pagerState,
                modifier = Modifier.weight(1f),
            )

            // Bottom CTA area — changes per slide
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ChurrosSpacing.pagePadding,
                        vertical = ChurrosSpacing.M,
                    ),
            ) {
                val isLastPage = pagerState.currentPage == ONBOARDING_PAGE_COUNT - 1
                if (!isLastPage) {
                    ChurrosButton(
                        text = stringResource(Res.string.onboarding_cta_next),
                        trailingIcon = ChurrosIcons.ArrowForward,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
                    ) {
                        ChurrosButton(
                            text = stringResource(Res.string.action_create_account),
                            onClick = {
                                viewModel.onEvent(OnboardingUiEvent.NavigateToSignUpClicked)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        ChurrosOutlinedButton(
                            text = stringResource(Res.string.action_sign_in),
                            onClick = {
                                viewModel.onEvent(OnboardingUiEvent.NavigateToSignInClicked)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(ChurrosSpacing.S))
        }

        // "Skip" text link — visible only on slides 1 and 2
        if (pagerState.currentPage < ONBOARDING_PAGE_COUNT - 1) {
            ChurrosTextButton(
                text = stringResource(Res.string.onboarding_cta_skip),
                onClick = { viewModel.onEvent(OnboardingUiEvent.SkipClicked) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = ChurrosSpacing.S, end = ChurrosSpacing.M),
            )
        }
    }
}

// ── Slide content ─────────────────────────────────────────────────────────────

/**
 * Content composable for a single onboarding slide.
 *
 * Displays a centred illustration placeholder (app logo) in the upper area and
 * the slide's title + body text below. Replace the [Image] block with real
 * illustration assets when they become available.
 */
@Composable
private fun OnboardingSlideContent(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = ChurrosSpacing.pagePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // TODO: replace with onboarding illustration assets
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = ChurrosSpacing.L),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_app_logo),
                contentDescription = null, // decorative placeholder
                modifier = Modifier.size(IllustrationPlaceholderSizeDp),
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(ChurrosSpacing.S))

        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = ChurrosSpacing.M),
        )
    }
}

// ── Constants ─────────────────────────────────────────────────────────────────

private const val ONBOARDING_PAGE_COUNT = 3
private val IllustrationPlaceholderSizeDp = 160.dp

// ── Previews ──────────────────────────────────────────────────────────────────

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingSlide1PreviewContent() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { ONBOARDING_PAGE_COUNT })
    OnboardingScreenPreviewLayout(pagerState = pagerState)
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingSlide2PreviewContent() {
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { ONBOARDING_PAGE_COUNT })
    OnboardingScreenPreviewLayout(pagerState = pagerState)
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun OnboardingSlide3PreviewContent() {
    val pagerState = rememberPagerState(initialPage = 2, pageCount = { ONBOARDING_PAGE_COUNT })
    OnboardingScreenPreviewLayout(pagerState = pagerState)
}

/**
 * Stateless layout helper for previews — mirrors [OnboardingScreen] but avoids
 * Koin injection so the preview composable can render in isolation.
 */
@Composable
private fun OnboardingScreenPreviewLayout(
    pagerState: androidx.compose.foundation.pager.PagerState,
) {
    val scope = rememberCoroutineScope()
    val pages: List<@Composable () -> Unit> = listOf(
        {
            OnboardingSlideContent(
                title = stringResource(Res.string.onboarding_slide1_title),
                body = stringResource(Res.string.onboarding_slide1_body),
            )
        },
        {
            OnboardingSlideContent(
                title = stringResource(Res.string.onboarding_slide2_title),
                body = stringResource(Res.string.onboarding_slide2_body),
            )
        },
        {
            OnboardingSlideContent(
                title = stringResource(Res.string.onboarding_slide3_title),
                body = stringResource(Res.string.onboarding_slide3_body),
            )
        },
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ChurrosOnboardingPager(
                pages = pages,
                pagerState = pagerState,
                modifier = Modifier.weight(1f),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ChurrosSpacing.pagePadding,
                        vertical = ChurrosSpacing.M,
                    ),
            ) {
                val isLastPage = pagerState.currentPage == ONBOARDING_PAGE_COUNT - 1
                if (!isLastPage) {
                    ChurrosButton(
                        text = stringResource(Res.string.onboarding_cta_next),
                        trailingIcon = ChurrosIcons.ArrowForward,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S)) {
                        ChurrosButton(
                            text = stringResource(Res.string.action_create_account),
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                        )
                        ChurrosOutlinedButton(
                            text = stringResource(Res.string.action_sign_in),
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(ChurrosSpacing.S))
        }

        if (pagerState.currentPage < ONBOARDING_PAGE_COUNT - 1) {
            ChurrosTextButton(
                text = stringResource(Res.string.onboarding_cta_skip),
                onClick = {},
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = ChurrosSpacing.S, end = ChurrosSpacing.M),
            )
        }
    }
}
