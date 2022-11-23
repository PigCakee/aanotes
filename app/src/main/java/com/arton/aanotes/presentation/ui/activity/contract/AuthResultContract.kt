package com.arton.aanotes.presentation.ui.activity.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.arton.aanotes.presentation.ui.activity.AuthActivity

class AuthResultContract : ActivityResultContract<AuthEvent, Int?>() {

    companion object {
        const val EXTRA_AUTH_EVENT = "extra_auth_event"
        const val AUTH_FAILED = 666
        const val AUTH_SUCCEEDED = 777
        const val AUTH_RESULT_KEY = "auth_result_key"
    }

    override fun createIntent(context: Context, input: AuthEvent): Intent {
        return Intent(context, AuthActivity::class.java).putExtra(EXTRA_AUTH_EVENT, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int? {
        return if (resultCode != Activity.RESULT_OK) null
        else intent?.extras?.getInt(AUTH_RESULT_KEY)
    }
}