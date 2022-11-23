package com.arton.aanotes.presentation.ui

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arton.aanotes.common.utils.SnackbarManager
import com.arton.aanotes.presentation.ui.composable.navigation.MainSections
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Destinations used in the [AANotesApp].
 */

object MainDestinations {
    const val EDITOR_ROUTE = "editor"
    const val NOTES_ROUTE = "notes"
    const val SETTINGS_ROUTE = "settings"
}

object AuthDestinations {
    const val SPLASH_ROUTE = "splash"
    const val ONBOARDING_ROUTE = "onboarding"
}

object EditorDestinations {
    const val CUSTOMIZE_ROUTE = "customize"
}

/**
 * Remembers and creates an instance of [AANotesAppState]
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberAANotesAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberAnimatedNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        AANotesAppState(
            scaffoldState,
            navController,
            snackbarManager,
            coroutineScope
        )
    }

/**
 * Responsible for holding state related to [AANotesApp] and containing UI-related logic.
 */
@Stable
class AANotesAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    coroutineScope: CoroutineScope,
) {
    // Process snackbars coming from SnackbarManager
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = message.message

                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    scaffoldState.snackbarHostState.showSnackbar(text)
                    // Once the snackbar is gone or dismissed, notify the SnackbarManager
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    // ----------------------------------------------------------
    // BottomBar state source of truth
    // ----------------------------------------------------------

    private val allBottomBarTabs = listOf(
        MainSections.EditorSection,
        MainSections.NotesSection,
        MainSections.SettingsSection
    )

    var currentBottomBarTabs = allBottomBarTabs

    private val bottomBarRoutes = currentBottomBarTabs.map { it.destination }

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}