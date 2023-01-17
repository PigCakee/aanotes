package com.arton.aanotes.common.utils

import java.text.SimpleDateFormat
import java.util.*

const val PATTERN_DD_MMM_YYYY_HH_MM = "dd MMM yyyy H:mm"
fun formatDateShort(date: Date?): String {
    return if (date == null) "" else SimpleDateFormat(PATTERN_DD_MMM_YYYY_HH_MM, Locale.getDefault()).format(date)
}