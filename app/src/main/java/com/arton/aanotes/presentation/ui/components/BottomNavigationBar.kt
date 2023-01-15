package com.arton.aanotes.presentation.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.arton.aanotes.presentation.ui.AANotesAppState
import com.arton.aanotes.presentation.ui.composable.navigation.MainSections
import com.arton.aanotes.presentation.ui.theme.BlueMain
import com.arton.aanotes.presentation.ui.theme.GreyDark
import com.arton.aanotes.presentation.ui.theme.Transparent95
import com.arton.aanotes.presentation.ui.theme.UiBackground

@Composable
fun BottomNavigationBar(
    appState: AANotesAppState,
) {
    val bottomBarItems = appState.currentBottomBarTabs

    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        elevation = 8.dp,
        backgroundColor = UiBackground.copy(alpha = Transparent95)
    ) {

        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomBarItems.forEach { item ->

            val selected = item.destination == currentRoute
            BottomNavigationItem(
                alwaysShowLabel = true,
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        softWrap = false,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = item.iconId),
                        contentDescription = stringResource(id = item.title)
                    )
                },
                selected = selected,
                onClick = {
                    appState.navController.navigate(item.destination) {
                        val navigationRoutes = bottomBarItems.map(MainSections::destination)
                        val firstBottomBarDestination = appState.navController.backQueue
                            .firstOrNull { navigationRoutes.contains(it.destination.route) }
                            ?.destination

                        firstBottomBarDestination?.let { route ->
                            popUpTo(route.id) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = BlueMain,
                unselectedContentColor = GreyDark,
            )
        }
    }
}