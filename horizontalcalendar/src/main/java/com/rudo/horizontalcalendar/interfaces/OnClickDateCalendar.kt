package com.rudo.horizontalcalendar.interfaces

import java.util.*

interface OnClickDateCalendar {
    fun onClickDate(position: Int, date: Date, isInExtraRange: Boolean, isSelected: Boolean, isDayPast: Boolean)
}