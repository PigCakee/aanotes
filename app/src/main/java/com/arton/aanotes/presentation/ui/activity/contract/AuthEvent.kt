package com.arton.aanotes.presentation.ui.activity.contract

import android.os.Parcelable
import com.arton.aanotes.R
import kotlinx.parcelize.Parcelize

sealed class AuthEvent : Parcelable {
    @Parcelize
    object AuthenticateAction: AuthEvent()

    @Parcelize
    object Login: AuthEvent()

    @Parcelize
    data class CreatePin(var pin1: String = "", var pin2: String = "") : AuthEvent()

    @Parcelize
    data class UpdatePin(var oldPin: String = "", var newPin: String = ""): AuthEvent()

    fun getTitleRes() = when(this) {
        is AuthenticateAction -> R.string.confirm_action
        is UpdatePin -> R.string.update_pin
        is Login -> R.string.login
        is CreatePin -> R.string.create_pin
    }

    fun getDescriptionRes(canUseBiometrics: Boolean) = when(this) {
        is AuthenticateAction -> if (canUseBiometrics) R.string.scan_or_enter_pin else R.string.enter_pin
        is UpdatePin -> if (oldPin.isBlank()) R.string.update_pin_old_pin else R.string.update_pin_2_step
        is Login -> if (canUseBiometrics) R.string.scan_or_enter_pin else R.string.enter_pin
        is CreatePin -> if (pin1.isBlank()) R.string.update_pin_new_pin else R.string.update_pin_2_step
    }
}