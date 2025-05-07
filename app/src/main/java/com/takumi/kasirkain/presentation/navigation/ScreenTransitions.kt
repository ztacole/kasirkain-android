package com.takumi.kasirkain.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object ScreenTransitions {

    fun slideTransition(): EnterTransition {
        return slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
    }

    fun slideExitTransition(): ExitTransition {
        return slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
    }

    fun fadeTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(300))
    }

    fun fadeExitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(300))
    }

    fun scaleTransition(): EnterTransition {
        return scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
    }

    fun scaleExitTransition(): ExitTransition {
        return scaleOut(targetScale = 1.2f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
    }
}
