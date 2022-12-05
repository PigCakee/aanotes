package com.arton.aanotes.presentation.ui.composable.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.AANotesAppState
import com.arton.aanotes.presentation.ui.AuthDestinations.ONBOARDING_ROUTE
import com.arton.aanotes.presentation.ui.AuthDestinations.SPLASH_ROUTE
import com.arton.aanotes.presentation.ui.EditorDestinations
import com.arton.aanotes.presentation.ui.MainDestinations
import com.arton.aanotes.presentation.ui.screen.editor.EditorScreen
import com.arton.aanotes.presentation.ui.screen.settings.SettingsScreen
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

sealed class GlobalSections(val destination: String) {
    object SplashSection : GlobalSections(SPLASH_ROUTE)
    object OnboardingSection : GlobalSections(ONBOARDING_ROUTE)
}

sealed class MainSections(
    @StringRes val title: Int,
    val destination: String,
    @DrawableRes val iconId: Int,
) {
    object EditorSection : MainSections(
        R.string.editor,
        MainDestinations.EDITOR_ROUTE,
        R.drawable.ic_editor
    )

    object NotesSection : MainSections(
        R.string.notes,
        MainDestinations.NOTES_ROUTE,
        R.drawable.ic_search
    )

    object SettingsSection : MainSections(
        R.string.settings,
        MainDestinations.SETTINGS_ROUTE,
        R.drawable.ic_settings
    )
}

sealed class NotesSubsections(val destination: String) {
    object CustomizeSection : NotesSubsections(EditorDestinations.CUSTOMIZE_ROUTE)
}

sealed class SettingsSubsections(val destination: String) {
    object CustomizeSection : SettingsSubsections(EditorDestinations.CUSTOMIZE_ROUTE)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AANotesNavGraph(
    appState: AANotesAppState,
    modifier: Modifier,
    startDestination: String = MainSections.EditorSection.destination,
    mainViewModel: MainViewModel,
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(tween(300)) },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = { fadeOut(tween(300)) }
    ) {
        composable(GlobalSections.OnboardingSection.destination) {

        }
        composable(MainSections.EditorSection.destination) {
            EditorScreen(editorViewModel = hiltViewModel())
        }
        composable(MainSections.NotesSection.destination) {

        }
        composable(MainSections.SettingsSection.destination) {
            SettingsScreen(
                settingsViewModel = hiltViewModel(),
                mainViewModel = mainViewModel
            )
        }
    }
}

val tweenSpec =
    tween<IntOffset>(durationMillis = 400, easing = CubicBezierEasing(0.08f, 0.93f, 0.08f, 1.37f))


