package com.rudo.horizontalcalendar.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {

        fun getDateFormatted(date: Date, pattern: String): String {
            val simpleDateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDateFormatter.format(date)
        }

    }

}