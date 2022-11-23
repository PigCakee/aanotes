package com.arton.aanotes.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.arton.aanotes.common.LOADING_ANIMATION_SIZE

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    composition: LottieComposition?
) {
    AnimatedVisibility(
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
        visible = isLoading
    ) {
        LottieAnimation(
            modifier = Modifier.size(LOADING_ANIMATION_SIZE.dp),
            restartOnPlay = true,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            composition = composition,
            isPlaying = isLoading,
            iterations = Int.MAX_VALUE,
            speed = 1f
        )
    }
}