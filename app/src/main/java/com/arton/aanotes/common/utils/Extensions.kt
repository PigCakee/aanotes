package com.arton.aanotes.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun Job.stop() {
    apply {
        cancelChildren()
        cancel()
    }
}

fun String.isValidEmail() =
    this.isNotEmpty()
//            && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Long.dateByPattern(pattern: String): String {
    val date = Date(this)
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}

fun String.encode(): String =
    URLEncoder.encode(this, StandardCharsets.UTF_8.toString()).replace("%20", "+")

fun String.decode(): String =
    URLDecoder.decode(this, StandardCharsets.UTF_8.toString()).replace(" ", "%20")

fun String.toLocalDatePattern(pattern: String): String =
    if (this.isEmpty()) this else DateTimeFormatter.ofPattern(pattern)
        .withZone(ZoneId.systemDefault()).format(LocalDateTime.parse(this))

fun Clock.nowIsoLocalTime(): String {
    return DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault())
        .format(ZonedDateTime.now(this))
}

fun String.isUpcoming(): Boolean =
    LocalDateTime.parse(this).atZone(ZoneId.systemDefault()).isAfter(ZonedDateTime.now())


fun String.toInstantPattern(pattern: String): String =
    DateTimeFormatter.ofPattern(pattern, Locale.US)
        .withZone(ZoneId.systemDefault()).format(LocalDateTime.parse(this))

fun String.toTimePatternPlus(pattern: String, durationMin: Int): String =
    DateTimeFormatter.ofPattern(pattern)
        .withZone(ZoneId.systemDefault())
        .format(LocalDateTime.parse(this).plusMinutes(durationMin.toLong()))

fun Int.minutesToHours(): Int = this.div(60)

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "00:00"
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}

const val PATTERN_DD_MMM_YYYY_HH_MM = "dd MMM yyyy H:mm"
fun formatDateShort(date: Date?): String {
    return if (date == null) "" else SimpleDateFormat(PATTERN_DD_MMM_YYYY_HH_MM).format(date)
}