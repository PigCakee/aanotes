package com.arton.aanotes.data.entity

sealed class Action {
    object TurnOnScreenCapture: Action()
    object TurnOffScreenCapture: Action()
    object TurnOnSharing: Action()
    object TurnOffSharing: Action()
    object ChangePin: Action()
}

data class ActionResult(
    val action: Action? = null,
    val authenticated: Boolean? = false
)
