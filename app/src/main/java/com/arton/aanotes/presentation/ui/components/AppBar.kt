package com.arton.aanotes.presentation.ui.components

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arton.aanotes.R
import com.arton.aanotes.common.APP_BAR_LOGO_HEIGHT
import com.arton.aanotes.common.ZERO
import com.arton.aanotes.presentation.ui.theme.Gray
import com.arton.aanotes.presentation.ui.theme.White

@Composable
fun AANotesAppBar(
    onNavigationClick: () -> Unit,
    onActionClick: () -> Unit = {},
    @DrawableRes navigationIconId: Int = R.drawable.ic_arrow_back_24,
    @DrawableRes titleImageId: Int = R.drawable.rethink_care_logo,
    @DrawableRes actionIconId: Int = R.drawable.ic_my_staff_24,
    iconsColor: Color = Gray,
    withActions: Boolean = false,
    @ColorRes backgroundColor: Color = White,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { onNavigationClick() }) {
                Icon(
                    painter = painterResource(id = navigationIconId),
                    contentDescription = stringResource(id = R.string.back_button),
                    tint = iconsColor
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    alignment = Alignment.Center,
                    painter = painterResource(id = titleImageId),
                    contentDescription = stringResource(id = R.string.company_logo),
                    modifier = Modifier.height(APP_BAR_LOGO_HEIGHT.dp)
                )
            }

        },
        actions =
        {
            if (withActions) {
                IconButton(
                    onClick = { onActionClick() }) {
                    Icon(
                        painter = painterResource(id = actionIconId),
                        contentDescription = stringResource(id = R.string.action_button),
                        tint = iconsColor
                    )
                }
            } else {
                HorizontalSpacer(width = 48.dp)
            }
        },
        backgroundColor = backgroundColor,
        elevation = 4.dp
    )
}
