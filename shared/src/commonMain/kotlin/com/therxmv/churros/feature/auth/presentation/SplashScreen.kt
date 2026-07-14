package com.therxmv.churros.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.action_create_account
import churros.shared.generated.resources.action_sign_in
import churros.shared.generated.resources.app_name
import churros.shared.generated.resources.ic_app_logo_24
import churros.shared.generated.resources.ic_sparkle
import churros.shared.generated.resources.splash_tagline_prefix
import churros.shared.generated.resources.splash_tagline_suffix
import churros.shared.generated.resources.splash_welcome_to
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.components.ChurrosLoadingIndicator
import com.therxmv.churros.core.design.components.ChurrosPrimaryButton
import com.therxmv.churros.core.design.components.ChurrosSecondaryButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val LogoBoxSize = 260.dp
private val LogoSize = 180.dp

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(lifecycleOwner, viewModel.effects) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.effects.collect { effect ->
                    when (effect) {
                        SplashEffect.NavigateToHome -> onNavigateToHome()
                    }
                }
            }
        }
    }

    if (isLoading) {
        SplashLoadingContent()
    } else {
        SplashScreenContent(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToRegister = onNavigateToRegister,
        )
    }
}

@Composable
private fun SplashLoadingContent() {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            ChurrosLoadingIndicator()
        }
    }
}

@Composable
fun SplashScreenContent(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = ChurrosSpacing.pagePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))

            LogoWithSparkles()

            Spacer(Modifier.height(ChurrosSpacing.M))

            Text(
                text = stringResource(Res.string.splash_welcome_to),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(ChurrosSpacing.S))

            val taglinePrefix = stringResource(Res.string.splash_tagline_prefix)
            val taglineSuffix = stringResource(Res.string.splash_tagline_suffix)
            Text(
                text = buildAnnotatedString {
                    append(taglinePrefix)
                    withStyle(SpanStyle(color = Honey500)) {
                        append(taglineSuffix)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.weight(1f))

            ChurrosPrimaryButton(
                text = stringResource(Res.string.action_sign_in),
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(ChurrosSpacing.S))

            ChurrosSecondaryButton(
                text = stringResource(Res.string.action_create_account),
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(ChurrosSpacing.L))
        }
    }
}

@Composable
private fun LogoWithSparkles() {
    Box(
        modifier = Modifier.size(LogoBoxSize),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_app_logo_24),
            contentDescription = null,
            modifier = Modifier.size(LogoSize),
        )
        // Large sparkle — top right
        Image(
            painter = painterResource(Res.drawable.ic_sparkle),
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-12).dp, y = 22.dp),
            colorFilter = ColorFilter.tint(Honey500),
        )
        // Small sparkle — top left
        Image(
            painter = painterResource(Res.drawable.ic_sparkle),
            contentDescription = null,
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.TopStart)
                .offset(x = 28.dp, y = 46.dp),
            colorFilter = ColorFilter.tint(Honey500),
        )
        // Medium sparkle — bottom left
        Image(
            painter = painterResource(Res.drawable.ic_sparkle),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.BottomStart)
                .offset(x = 18.dp, y = (-46).dp),
            colorFilter = ColorFilter.tint(Honey500),
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun SplashScreenPreview() {
    SplashScreenContent(
        onNavigateToLogin = {},
        onNavigateToRegister = {},
    )
}
