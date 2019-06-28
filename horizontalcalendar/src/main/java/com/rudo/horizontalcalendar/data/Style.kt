package com.rudo.horizontalcalendar.data

import android.support.annotation.ColorRes

class Style {

    data class StyleCalendar(
        val basicStyle: BasicStyle,
        var textStyle: BasicStyle?,
        var selectedStyle: SelectedStyle?,
        var daysSelectedStyle: DaysSelectedStyle?, @ColorRes var colorNameText: Int?
    )

    data class BasicStyle(
        @ColorRes val colorDaysBefore: Int, @ColorRes val colorDaysAfter: Int, @ColorRes val colorDaySelected: Int, val colorDayToday: Int
    )

    data class SelectedStyle(@ColorRes val colorRange: Int, @ColorRes val textColorRange: Int)

    data class DaysSelectedStyle(@ColorRes val colorDot: Int)
}