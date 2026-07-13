package com.therxmv.churros.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import churros.shared.generated.resources.app_name
import churros.shared.generated.resources.app_tagline
import churros.shared.generated.resources.ic_app_logo_24
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.components.ChurrosPrimaryButton
import com.therxmv.churros.core.design.components.ChurrosSecondaryButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val IllustrationSize = 200.dp

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current

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

    SplashScreenContent(
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToRegister = onNavigateToRegister,
    )
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
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_app_logo_24),
                contentDescription = null,
                modifier = Modifier.size(IllustrationSize),
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.XL))

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.S))

            Text(
                text = stringResource(Res.string.app_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = Honey500,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.XXL))

            ChurrosPrimaryButton(
                text = stringResource(Res.string.action_sign_in),
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(ChurrosSpacing.S))

            ChurrosSecondaryButton(
                text = stringResource(Res.string.action_create_account),
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
            )
        }
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
