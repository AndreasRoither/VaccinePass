package com.mobilehealthsports.vaccinepass.ui.main.calendar

import android.view.View
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mobilehealthsports.vaccinepass.databinding.CalendarDayBinding

class DayViewContainer(view: View, val onClick: (CalendarDay) -> Unit) : ViewContainer(view) {
    // Will be set when this container is bound. See the dayBinder.
    lateinit var day: CalendarDay
    val textView = CalendarDayBinding.bind(view).calendarDayText

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                onClick(day)
            }
        }
    }
}