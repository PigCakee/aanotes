package com.arton.aanotes.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arton.aanotes.presentation.ui.theme.PrimaryTextColor
import com.google.relay.compose.BoxScopeInstanceImpl.align

@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    title: String? = null,
    text: String,
    onDismiss: () -> Unit,
    buttons: @Composable () -> Unit
) {
    AnimatedVisibility(visible = showDialog) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismiss() },
            title = if (title is String) {
                {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = PrimaryTextColor
                    )
                }
            } else null,
            text = {
                Text(
                    text,
                    color = PrimaryTextColor,
                    textAlign = TextAlign.Center
                )
            },
            shape = RoundedCornerShape(16.dp),
            buttons = buttons
        )
    }
}