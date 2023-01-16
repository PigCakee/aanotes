package com.arton.aanotes.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.arton.aanotes.presentation.ui.theme.Black

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    @DrawableRes iconRes: Int,
    textColor: Color = Black,
    iconColor: Color = Black,
    content: @Composable BoxScope.() -> Unit
) {

    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (icon, title, endContent) = createRefs()

        Icon(
            modifier = Modifier.constrainAs(icon) {
                top.linkTo(parent.top, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 24.dp)
                start.linkTo(parent.start, margin = 24.dp)
            },
            painter = painterResource(id = iconRes),
            contentDescription = "",
            tint = iconColor
        )

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 24.dp)
                start.linkTo(icon.end, margin = 12.dp)
                end.linkTo(endContent.start, margin = 12.dp)
                width = Dimension.fillToConstraints
            },
            text = text,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp
            )
        )

        Box(modifier = Modifier.constrainAs(endContent) {
            top.linkTo(parent.top, margin = 24.dp)
            bottom.linkTo(parent.bottom, margin = 24.dp)
            end.linkTo(parent.end, margin = 24.dp)
        }) {
            content()
        }
    }
}