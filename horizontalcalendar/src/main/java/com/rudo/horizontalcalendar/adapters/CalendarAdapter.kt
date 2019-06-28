package com.rudo.horizontalcalendar.adapters

import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.rudo.horizontalcalendar.R
import com.rudo.horizontalcalendar.adapters.CalendarAdapter.ViewHolderDay
import com.rudo.horizontalcalendar.data.Style
import com.rudo.horizontalcalendar.entities.Day
import com.rudo.horizontalcalendar.inflate
import com.rudo.horizontalcalendar.isSameDay
import kotlinx.android.synthetic.main.item_calendar_day.view.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(
    var days: ArrayList<Day>,
    var width: Int,
    style: Style.StyleCalendar,
    private val onclickDate: (position: Int, date: Date, isExtraRange: Boolean, isSelected: Boolean, isDayPast: Boolean) -> Unit
) : RecyclerView.Adapter<ViewHolderDay>() {


    private val styleBasic = style
    private var daySelected = Calendar.getInstance()


    override fun getItemCount(): Int {
        return days.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDay =
        ViewHolderDay(parent.inflate(R.layout.item_calendar_day), styleBasic, onclickDate, daySelected, { date: Date ->
            daySelected.time = date
        })


    override fun onBindViewHolder(holder: ViewHolderDay, position: Int) =
        holder.bind(position, days[position], width)


    class ViewHolderDay(
        itemView: View,
        private val style: Style.StyleCalendar,
        private val onClickDate: (position: Int, date: Date, isExtraRange: Boolean, isSelected: Boolean, isDayPast: Boolean) -> Unit,
        private val daySelected: Calendar,
        private val changeDay: (Date) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int, day: Day, width: Int) = with(itemView) {
            itemView.minimumWidth = width

            val calendar = Calendar.getInstance()
            calendar.time = day.date

            val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
            txtDay.text = LetterDay.values()[dayOfWeek - 1].toString()
            style.colorNameText?.let {
                txtDay.setTextColor(ContextCompat.getColor(context, it))
            }

            txtNumDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()



            if (day.isDone) { //check if the day is a day before today
                val bgShape = backDay.background as GradientDrawable
                bgShape.setColor(ContextCompat.getColor(context, style.basicStyle.colorDaysBefore))
                style.textStyle?.let {
                    txtNumDay.setTextColor(ContextCompat.getColor(context, it.colorDaysBefore))
                }

            } else { //is a next day
                val bgShape = backDay.background as GradientDrawable
                bgShape.setColor(ContextCompat.getColor(context, style.basicStyle.colorDaysAfter))
                style.textStyle?.let {
                    txtNumDay.setTextColor(ContextCompat.getColor(context, it.colorDaysAfter))
                }
            }


            if (day.isSelected) {
                /*viewSelected.visibility = View.VISIBLE
                style.daysSelectedStyle?.let {
                    val bgShape = viewSelected.background as GradientDrawable
                    bgShape.setColor(ContextCompat.getColor(context, it.colorDot))
                }*/
                val bgShape = backDay.background as GradientDrawable
                bgShape.setColor(ContextCompat.getColor(context, style.basicStyle.colorDaySelected))
                style.textStyle?.let {
                    txtNumDay.setTextColor(ContextCompat.getColor(context, it.colorDaySelected))
                }
                day.isSelected = false
            }
            /*else {
                viewSelected.visibility = View.INVISIBLE
            }*/

            if (day.isExtraRange) {
                style.selectedStyle?.let {
                    val bgShape = backDay.background as GradientDrawable
                    bgShape.setColor(ContextCompat.getColor(context, it.colorRange))
                    txtNumDay.setTextColor(ContextCompat.getColor(context, it.textColorRange))
                }

            }

            if (calendar.isSameDay(Calendar.getInstance())) { //is today
                viewSelected.visibility = View.VISIBLE

                val bgShape = backDay.background as GradientDrawable
                bgShape.setColor(ContextCompat.getColor(context, style.basicStyle.colorDayToday))
                style.textStyle?.let {
                    txtNumDay.setTextColor(ContextCompat.getColor(context, it.colorDayToday))
                }

            } else {
                viewSelected.visibility = View.INVISIBLE
            }

            if (calendar.isSameDay(daySelected)) { //check if the day is a selected day
                val bgShape = backDay.background as GradientDrawable
                bgShape.setColor(ContextCompat.getColor(context, style.basicStyle.colorDaySelected))
                style.textStyle?.let {
                    txtNumDay.setTextColor(ContextCompat.getColor(context, it.colorDaySelected))
                }
            }

            itemDay.setOnClickListener {
                if (!day.isDone) {
                    changeDay.invoke(day.date)
                }
                if (!day.isSelected) {
                    day.isSelected = true
                }
                daySelected.time = day.date
                onClickDate.invoke(position, day.date, day.isExtraRange, day.isSelected, day.isDone)
            }

        }
    }

    enum class LetterDay {
        D, L, M, X, J, V, S
    }

}

