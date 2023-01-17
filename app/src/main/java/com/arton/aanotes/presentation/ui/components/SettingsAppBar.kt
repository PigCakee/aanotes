package com.arton.aanotes.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.theme.White

@Composable
fun SettingsAppBar(
    @StringRes titleResId: Int = R.string.settings,
) {
    Surface(
        color = White,
        elevation = 4.dp,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val (title) = createRefs()

            Text(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                text = stringResource(id = titleResId),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview
@Composable
fun PreviewAppBar() {
    AANotesTheme {
        SettingsAppBar()
    }
}
