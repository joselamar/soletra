package com.developer.lamarao.soletra.core.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.developer.lamarao.soletra.appDestination
import com.developer.lamarao.soletra.destinations.GameScreenDestination
import com.developer.lamarao.soletra.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

object Transitions : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition =
        when (initialState.appDestination()) {
            HomeScreenDestination ->
                slideInHorizontally(
                    initialOffsetX = { -Offset },
                    animationSpec = tween(AnimationDuration)
                )

            GameScreenDestination -> slideInHorizontally(
                initialOffsetX = { Offset },
                animationSpec = tween(AnimationDuration)
            )

            else -> fadeIn()
        }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition =
        when (initialState.appDestination()) {
            HomeScreenDestination -> slideOutHorizontally(
                targetOffsetX = { Offset },
                animationSpec = tween(AnimationDuration)
            )

            GameScreenDestination -> slideOutHorizontally(
                targetOffsetX = { -Offset },
                animationSpec = tween(AnimationDuration)
            )

            else -> fadeOut()
        }

    private const val Offset = 3000
    private const val AnimationDuration = 500
}
