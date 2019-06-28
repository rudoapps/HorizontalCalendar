package com.rudo.horizontalcalendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.*


fun Int.toString(context: Context): String {
    return context.getString(this)
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadDrawable(idDrawable: Int) {
    this.setImageDrawable(ContextCompat.getDrawable(context, idDrawable))
}

fun String.trimOnlyLeadingandTrailing(): String {
    return this.replace("^\\s+|\\s+$", "")
}

fun Calendar.isSameDay(otherdate: Calendar): Boolean {
    return (this.get(Calendar.YEAR) == otherdate.get(Calendar.YEAR)
            && this.get(Calendar.MONTH) == otherdate.get(Calendar.MONTH)
            && this.get(Calendar.DAY_OF_MONTH) == otherdate.get(Calendar.DAY_OF_MONTH))
}

