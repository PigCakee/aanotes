package com.arton.aanotes.domain.entity

sealed class Action {
    object TurnOnScreenCapture: Action()
    object TurnOnSharing: Action()
    object ChangePin: Action()
    object DeleteAllData: Action()
}

data class ActionResult(
    val action: Action? = null,
    val authenticated: Boolean? = false
)