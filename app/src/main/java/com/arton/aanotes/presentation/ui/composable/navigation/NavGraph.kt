package com.arton.aanotes.presentation.ui.composable.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.AANotesAppState
import com.arton.aanotes.presentation.ui.MainDestinations
import com.arton.aanotes.presentation.ui.screen.editor.EditorScreen
import com.arton.aanotes.presentation.ui.screen.search.SearchScreen
import com.arton.aanotes.presentation.ui.screen.settings.SettingsScreen
import com.arton.aanotes.presentation.ui.viewmodel.MainViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

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
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(MainSections.EditorSection.destination) {
            EditorScreen(
                editorViewModel = hiltViewModel(),
                mainViewModel = mainViewModel
            )
        }
        composable(MainSections.NotesSection.destination) {
            SearchScreen(searchViewModel = hiltViewModel()) {
                appState.navController.navigateUp()
            }
        }
        composable(MainSections.SettingsSection.destination) {
            SettingsScreen(
                settingsViewModel = hiltViewModel(),
                mainViewModel = mainViewModel
            )
        }
    }
}

