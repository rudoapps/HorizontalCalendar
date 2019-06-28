package com.rudo.horizontalcalendar

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.support.annotation.ColorRes
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.rudo.horizontalcalendar.adapters.CalendarAdapter
import com.rudo.horizontalcalendar.data.Constants
import com.rudo.horizontalcalendar.data.Style
import com.rudo.horizontalcalendar.entities.Day
import com.rudo.horizontalcalendar.interfaces.OnClickDateCalendar
import java.util.*
import kotlin.collections.ArrayList


class HorizontalCalendar(private val build: Build) {

    private var view: HorizontalCalendarView
    var totalDays: Int? = null
    private lateinit var listDays: ArrayList<Day>
    private lateinit var lastDaySelected: Day

    init {

        view = build.calendarView
        view.layoutManager = GridLayoutManager(build.context, 1, GridLayoutManager.HORIZONTAL, false)

        //divide screen in x number of items
        val size = Point()
        (build.context as Activity).windowManager.defaultDisplay.getSize(size)
        val screenWidth = size.x

        view.adapter = CalendarAdapter(
            getDays(null),
            screenWidth / build.daysInScreen,
            build.styleCalendar
        ) { position: Int, date: Date, b: Boolean, b1: Boolean, b2: Boolean ->

            build.onClick?.let {
                it.onClickDate(position, date, b, b1, b2)
            }
            view.adapter!!.notifyDataSetChanged()
            //refreshAdapter(date)

        }

        totalDays?.let {
            val middleDays = Math.floor(it / 2.toDouble())
            Log.d("prueba", "mitad days: " + middleDays)
            var positionCenter = middleDays

            when (build.gravityDaySelected) {

                GRAVITY.RIGHT -> {
                    var rightScreen = Math.floor((build.daysInScreen).toDouble())
                    Log.d("prueba", "rigthScreen screen: " + rightScreen)
                    positionCenter = (middleDays - rightScreen) + 1
                }

                GRAVITY.CENTER -> {
                    var middleScreen = Math.floor((build.daysInScreen / 2).toDouble())
                    Log.d("prueba", "mitad screen: " + middleScreen)
                    positionCenter = middleDays - middleScreen
                }
                else -> {

                }
            }

            Handler().post {
                view.layoutManager!!.scrollToPosition(positionCenter.toInt())
            }
        }


    }

    /*fun refreshAdapter(dateSelected : Date){

        //Division de la pantalla en x numero de items
        val size = Point()
        (build.context as Activity).windowManager.defaultDisplay.getSize(size)
        val screenWidth = size.x

        view.adapter = CalendarAdapter(
                getDays(dateSelected),
                screenWidth/build.daysInScreen,
                build.styleCalendar,
                { date: Date, b: Boolean, b1: Boolean ->

                    build.onClick?.let {
                        it.onClickDate(date,b,b1)
                    }
                    refreshAdapter(date)

                }
        )

        view.adapter.notifyDataSetChanged()

    }*/

    fun moveCalendarOnClick(position: Int) {

        val daySelected = listDays[position]

        val middleDays = position.toDouble()
        Log.d("prueba", "mitad days: " + middleDays)
        var positionCenter = middleDays

        when (build.gravityDaySelected) {

            GRAVITY.RIGHT -> {
                val rightScreen = Math.floor((build.daysInScreen).toDouble())
                Log.d("prueba", "rigthScreen screen: " + rightScreen)
                positionCenter = (middleDays - rightScreen) + 1
                /*if (daySelected.date.before(lastDaySelected.date)) {
                    positionCenter = midleDays - rigthScreen
                } else {
                    positionCenter = midleDays + rigthScreen
                }*/
            }

            GRAVITY.CENTER -> {
                val middleScreen = Math.floor((build.daysInScreen / 2).toDouble())
                Log.d("prueba", "mitad screen: " + middleScreen)
                positionCenter = if (daySelected.date.before(lastDaySelected.date)) {
                    middleDays - middleScreen
                } else {
                    middleDays + middleScreen
                }
            }
            else -> {

            }
        }

        Handler().post {
            view.layoutManager!!.scrollToPosition(positionCenter.toInt())
        }

        lastDaySelected = listDays[position]

    }

    private fun getDays(dateSelect: Date?): ArrayList<Day> {

        var rangeNum = Constants.DEFAULT_DAYS_RANGE //30 days by default
        build.rangeMaxNum?.let {
            //total number of days
            rangeNum = it
        }

        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        val calendarNow = Calendar.getInstance()

        lastDaySelected = Day(calendarNow.time, true, isDone = false, isExtraRange = false)


        dateSelect?.let {
            calendarNow.time = dateSelect
        }

        when (build.rangeMaxType) {
            TimeMeasure.DAY -> {
                calendarStart.add(Calendar.DAY_OF_MONTH, rangeNum * -1)
                calendarEnd.add(Calendar.DAY_OF_MONTH, rangeNum)
            }
            TimeMeasure.MONTH -> {
                calendarStart.add(Calendar.MONTH, rangeNum * -1)
                calendarEnd.add(Calendar.MONTH, rangeNum)
            }
            TimeMeasure.YEAR -> {
                calendarStart.add(Calendar.YEAR, rangeNum * -1)
                calendarEnd.add(Calendar.YEAR, rangeNum)
            }
            else -> { //is null. keep default option
                calendarStart.add(Calendar.DAY_OF_MONTH, rangeNum * -1)
                calendarEnd.add(Calendar.DAY_OF_MONTH, rangeNum)
            }
        }

        listDays = ArrayList()

        val difference = calendarEnd.time.time - calendarStart.time.time
        val daysBetween = (difference / (1000 * 60 * 60 * 24)).toFloat()
        val calendarRange: Calendar = calendarStart

        Log.d("prueba", "Dias diferencia: " + daysBetween)
        totalDays = daysBetween.toInt()

        for (i in 0..totalDays!!) {

            var isDone = false
            if (calendarRange.before(calendarNow)) {
                isDone = true
            }

            //check if day is in extra range
            var isExtraRange = false
            build.periodStart?.let {

                val start = Calendar.getInstance()
                start.time = it
                val end = Calendar.getInstance()
                end.time = build.periodEnd
                if (calendarRange.after(start) && calendarRange.before(end)) {
                    isExtraRange = true
                }

            }

            //check if it is selected
            var isSelected = false
            build.selectedDays?.let {

                it.onEach {
                    val daySelected = Calendar.getInstance()
                    daySelected.time = it
                    if (calendarRange.isSameDay(daySelected)) {
                        isSelected = true
                    }
                }

            }

            listDays.add(Day(calendarRange.time, isSelected, isDone, isExtraRange))
            calendarRange.add(Calendar.DAY_OF_MONTH, 1)

        }

        return listDays
    }


    class Build(internal var calendarView: HorizontalCalendarView, root: Context, style: Style.BasicStyle) {

        internal var context: Context = root
        internal var rangeMaxType: TimeMeasure? = null
        internal var gravityDaySelected: GRAVITY? = null
        internal var rangeMaxNum: Int? = null
        internal var daysInScreen: Int = Constants.DEFAULT_DAYS_IN_SCREEN
        internal var periodStart: Date? = null
        internal var periodEnd: Date? = null
        internal var selectedDays: ArrayList<Date>? = null
        internal var styleCalendar: Style.StyleCalendar = Style.StyleCalendar(style, null, null, null, null)
        internal var onClick: OnClickDateCalendar? = null

        fun colorNameDay(@ColorRes colorname: Int): Build {
            styleCalendar.colorNameText = colorname
            return this
        }

        fun colorText(styleText: Style.BasicStyle): Build {
            styleCalendar.textStyle = styleText
            return this
        }

        fun rangeMax(maxnum: Int, maxtype: TimeMeasure): Build {
            this.rangeMaxNum = maxnum
            this.rangeMaxType = maxtype
            return this
        }

        fun periodSelected(
            periodStart: Date,
            periodEnd: Date, @ColorRes colorSelected: Int, @ColorRes textcolorSelected: Int
        ): Build {
            this.periodStart = periodStart
            this.periodEnd = periodEnd
            this.styleCalendar.selectedStyle = Style.SelectedStyle(colorSelected, textcolorSelected)
            return this
        }

        fun selectedDays(listdays: ArrayList<Date>, @ColorRes colorSelected: Int): Build {
            this.selectedDays = listdays
            this.styleCalendar.daysSelectedStyle = Style.DaysSelectedStyle(colorSelected)
            return this
        }

        fun daysInScreen(numDays: Int): Build {
            this.daysInScreen = numDays
            return this
        }

        fun onClickDay(listener: OnClickDateCalendar): Build {
            this.onClick = listener
            return this
        }

        fun setGravityDaySelected(gravity: GRAVITY): Build {
            this.gravityDaySelected = gravity
            return this
        }

        fun build(): HorizontalCalendar {
            return HorizontalCalendar(this)
        }

    }

    enum class TimeMeasure {
        DAY, MONTH, YEAR
    }

    enum class GRAVITY {
        LEFT, CENTER, RIGHT
    }

}