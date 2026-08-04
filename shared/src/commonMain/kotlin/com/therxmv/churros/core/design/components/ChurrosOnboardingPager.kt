package com.therxmv.churros.core.design.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.onboarding_page_indicator_desc
import com.therxmv.churros.core.design.ChurrosMotion
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosSpacing
import org.jetbrains.compose.resources.stringResource

private val DotHeightDp = 6.dp
private val DotActiveWidthDp = 24.dp
private val DotInactiveWidthDp = 6.dp

/**
 * Full-screen swipeable onboarding carousel with animated indicator dots.
 *
 * Each dot widens to [DotActiveWidthDp] when its page is current.
 *
 * @param pages    Ordered list of composable page content lambdas.
 * @param modifier Applied to the outer [Column].
 */
@Composable
fun ChurrosOnboardingPager(
    pages: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            pages[page]()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = ChurrosSpacing.L),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pages.size) { index ->
                val indicatorDesc = stringResource(
                    Res.string.onboarding_page_indicator_desc,
                    index + 1,
                    pages.size,
                )
                val isSelected = pagerState.currentPage == index
                val dotWidth by animateDpAsState(
                    targetValue = if (isSelected) DotActiveWidthDp else DotInactiveWidthDp,
                    animationSpec = tween(durationMillis = ChurrosMotion.durationDefault),
                    label = "dot_width_$index",
                )
                val dotColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = ChurrosSpacing.XS)
                        .width(dotWidth)
                        .height(DotHeightDp)
                        .background(
                            color = dotColor,
                            shape = RoundedCornerShape(50),
                        )
                        .semantics { contentDescription = indicatorDesc },
                )
            }
        }
    }
}

@ChurrosPreview
@Composable
fun OnboardingPagerPreviewContent() {
    val pages = listOf<@Composable () -> Unit>(
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Welcome to Churros",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "A shared home starts with shared responsibilities.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = ChurrosSpacing.S),
                    )
                }
            }
        },
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Share chores, not the stress",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Everything your home needs",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
    )
    ChurrosOnboardingPager(
        pages = pages,
        modifier = Modifier.fillMaxSize(),
    )
}
