package com.example.wanderpedia.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderpedia.R

data class OnboardingPage(
    val title: String, val description: String, val imageRes: Int
)

val pages = listOf(
    OnboardingPage(
        title = "Discover Wonders",
        description = "Explore the wonders of the world and learn fascinating details about them.",
        imageRes = R.drawable.discover
    ), OnboardingPage(
        title = "Bookmark & Notes",
        description = "Save your favorite places and jot down your thoughts.",
        imageRes = R.drawable.bookmark
    ), OnboardingPage(
        title = "Navigate & Weather",
        description = "See maps, navigation, and live weather updates for each wonder.",
        imageRes = R.drawable.map
    )
)

@Composable
fun OnboardingPageContent(page: OnboardingPage, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = page.title, fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = page.description, fontSize = 16.sp, color = Color.Gray
            )
        }
    }
}