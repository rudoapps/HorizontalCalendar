package com.rudo.horizontalcalendar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rudo.horizontalcalendar.data.Style
import com.rudo.horizontalcalendar.interfaces.OnClickDateCalendar
import com.rudo.horizontalcalendar.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnClickDateCalendar {

    private lateinit var horizontalCalendar: HorizontalCalendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horizontalCalendar = HorizontalCalendar.Build(
            calendarView,
            this,
            Style.BasicStyle(
                android.R.color.darker_gray,
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                android.R.color.transparent
            )
        )
            .colorText(
                Style.BasicStyle(
                    android.R.color.white,
                    android.R.color.white,
                    android.R.color.white,
                    android.R.color.black
                )
            )
            .rangeMax(3, HorizontalCalendar.TimeMeasure.DAY)
            .daysInScreen(7)
            .onClickDay(this)
            .setGravityDaySelected(HorizontalCalendar.GRAVITY.CENTER)
            .build()

        //init today date
        setDate(Calendar.getInstance().time)

        configOnClicks()

    }

    private fun configOnClicks() {
        buttonPreviousWeek.setOnClickListener { horizontalCalendar.previousWeek() }
        buttonNextWeek.setOnClickListener { horizontalCalendar.nextWeek() }
    }

    private fun setDate(date: Date) {
        textDay.text = Utils.getDateFormatted(date, "EEEE, d 'de' MMMM 'de' yyyy")
    }

    override fun onClickDate(
        position: Int,
        date: Date,
        isInExtraRange: Boolean,
        isSelected: Boolean,
        isDayPast: Boolean
    ) {
        setDate(date)
    }

}
