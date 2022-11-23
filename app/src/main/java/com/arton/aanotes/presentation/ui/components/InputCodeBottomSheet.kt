package com.arton.aanotes.presentation.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import com.arton.aanotes.R
import com.arton.aanotes.presentation.ui.theme.AANotesTheme
import com.arton.aanotes.presentation.ui.theme.MainGrey
import com.arton.aanotes.presentation.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val PIN_CODE_LENGTH = 6

@Composable
fun InputCodeBottomSheet(
    modifier: Modifier = Modifier,
    onPinEntered: (String) -> Boolean = { false }
) {
    val vibratorManager = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var passcode by remember {
        mutableStateOf("")
    }
    var numbersEntered by remember {
        mutableStateOf(passcode.length)
    }
    if (numbersEntered == PIN_CODE_LENGTH) {
        val isCorrectPin = onPinEntered(passcode)
        passcode = ""
        numbersEntered = 0
        if (!isCorrectPin) {
            vibratorManager.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
            animatePins(offsetX, scope)
        }
    }

    Box(
        modifier.background(
            color = White,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier
                    .wrapContentSize()
                    .offset(offsetX.value.dp, 0.dp)
            ) {
                items(PIN_CODE_LENGTH) { index ->
                    Image(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        painter = painterResource(id = if (index + 1 <= numbersEntered) R.drawable.ic_pin_full else R.drawable.ic_pin_empty),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            DialRow(numbers = arrayOf(1, 2, 3), vibrator = vibratorManager, onButtonClick = {
                passcode += it.toString()
                numbersEntered++
            })
            Spacer(modifier = Modifier.height(20.dp))
            DialRow(numbers = arrayOf(4, 5, 6), vibrator = vibratorManager, onButtonClick = {
                passcode += it.toString()
                numbersEntered++
            })
            Spacer(modifier = Modifier.height(20.dp))
            DialRow(numbers = arrayOf(7, 8, 9), vibrator = vibratorManager, onButtonClick = {
                passcode += it.toString()
                numbersEntered++
            })
            Spacer(modifier = Modifier.height(20.dp))
            DialRow(numbers = arrayOf(0), vibrator = vibratorManager, onButtonClick = {
                passcode += it.toString()
                numbersEntered++
            })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DialRow(
    modifier: Modifier = Modifier,
    numbers: Array<Int>,
    vibrator: Vibrator,
    onButtonClick: (Int) -> Unit = {}
) {
    LazyRow(modifier = modifier) {
        items(numbers.size) { index ->
            Button(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MainGrey
                ),
                shape = CircleShape.copy(all = CornerSize(24.dp)),
                elevation = ButtonDefaults.elevation(0.dp),
                onClick = {
                    onButtonClick(numbers[index])
                    vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            ) {
                Text(
                    text = "${numbers[index]}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private val shakeKeyframes: AnimationSpec<Float> = keyframes {
    durationMillis = 400
    val easing = FastOutLinearInEasing

    // generate 8 keyframes
    for (i in 1..10) {
        val x = when (i % 3) {
            0 -> 6f
            1 -> -6f
            else -> 0f
        }
        x at durationMillis / 10 * i with easing
    }
}

private fun animatePins(
    offset: Animatable<Float, AnimationVector1D>,
    coroutineScope: CoroutineScope,
) {
    coroutineScope.launch {
        offset.animateTo(
            targetValue = 0f,
            animationSpec = shakeKeyframes,
        )
    }
}

@Preview
@Composable
fun PreviewInputCodeBottomSheet() {
    AANotesTheme {
        InputCodeBottomSheet(onPinEntered = { true })
    }
}
