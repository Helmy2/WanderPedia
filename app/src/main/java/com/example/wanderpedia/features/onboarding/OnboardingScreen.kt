package com.example.wanderpedia.features.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wanderpedia.core.ui.component.DefaultButton
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    navigateHome: () -> Unit
) {
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 56.dp)
            .padding(16.dp),
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) { page ->
            OnboardingPageContent(
                page = pages[page], modifier = Modifier
                    .height(500.dp)
                    .padding(16.dp)
            )
        }

        AnimatedContent(pagerState.currentPage == pages.size - 1) {
            if (it) {
                DefaultButton(onClick = {
                    viewModel.createAnonymousAccount()
                    navigateHome()
                }, isPrimary = true) {
                    Text(text = "Get Started")
                }
            } else {
                DefaultButton(onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }) {
                    Text(text = "Next")
                }
            }
        }
    }
}





