package com.arton.aanotes.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.common.CORNERS_RADIUS_10
import com.arton.aanotes.presentation.ui.theme.PrimaryTextColor

@Composable
fun Dialog(
    showDialog: Boolean,
    title: String? = null,
    text: String,
    onDismiss: () -> Unit,
    buttons: @Composable () -> Unit
) {
    AnimatedVisibility(visible = showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = if (title is String) {
                {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryTextColor
                    )
                }
            } else null,
            text = {
                Text(text, color = PrimaryTextColor)
            },
            shape = RoundedCornerShape(CORNERS_RADIUS_10.dp),
            buttons = buttons
        )
    }
}