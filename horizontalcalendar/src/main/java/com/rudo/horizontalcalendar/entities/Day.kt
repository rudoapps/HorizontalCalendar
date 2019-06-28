package com.rudo.horizontalcalendar.entities

import java.util.*

data class Day (val date: Date, var isSelected: Boolean = false, var isDone: Boolean, var isExtraRange: Boolean = false)