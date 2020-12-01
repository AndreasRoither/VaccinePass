package com.mobilehealthsports.vaccinepass.ui.main.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.daysOfWeekFromLocale
import com.mobilehealthsports.vaccinepass.util.setTextColorRes
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarViewModel : BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()
    val navigationRequest = ServiceRequest<NavigationRequest>()

    private var selectedDate = LocalDate.now()
    private var lastSelectedDay: CalendarDay? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.YY")
    private val events = mutableMapOf<LocalDate, List<Event>>()
    private lateinit var calendar: com.kizitonwose.calendarview.CalendarView

    private var _month = MutableLiveData(monthTitleFormatter.format(LocalDate.now().yearMonth))
    var month: LiveData<String> = _month

    private var _year = MutableLiveData(LocalDate.now().yearMonth.year.toString())
    var year: LiveData<String> = _year

    private var _selectedDayHeader = MutableLiveData(dateFormatter.format(LocalDate.now()))
    var selectedDayHeader: LiveData<String> = _selectedDayHeader

    val eventsAdapter = CalendarDayAdapter {
        messageRequest.request(ToastRequest("Clicked " + it.text))
    }

    fun setupCalendar(calendar: com.kizitonwose.calendarview.CalendarView) {
        // calendar
        this.calendar = calendar
        val daysOfWeek = daysOfWeekFromLocale()

        // TODO: use repository to get data
        events[LocalDate.now()] = listOf(Event("1", "Hello there", LocalDate.now()))

        eventsAdapter.apply {
            events.addAll(this@CalendarViewModel.events[LocalDate.now()].orEmpty())
            notifyDataSetChanged()
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        calendar.setup(startMonth, endMonth, daysOfWeek.first())
        calendar.scrollToMonth(currentMonth)

        calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) =
                DayViewContainer(view, this@CalendarViewModel::selectDate)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        events.containsKey(day.date) -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.drawable_circle_full)
                        }
                        selectedDate == day.date -> {
                            textView.setTextColorRes(R.color.app_primary)
                            textView.setBackgroundResource(R.drawable.drawable_circle_border)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.grey)
                    textView.background = null
                }
            }
        }

        calendar.monthScrollListener = {
            if (calendar.maxRowCount == 6) {
                _year.value = it.yearMonth.year.toString()
                _month.value = monthTitleFormatter.format(it.yearMonth)
            } else {
                // In week mode, we show the header a bit differently.
                // We show indices with dates from different months since
                // dates overflow and cells in one index can belong to different
                // months/years.
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    _year.value = firstDate.yearMonth.year.toString()
                    _month.value = monthTitleFormatter.format(firstDate)
                } else {
                    val monthText = "${monthTitleFormatter.format(firstDate)} - ${
                        monthTitleFormatter.format(
                            lastDate
                        )
                    }"
                    _month.value = monthText

                    if (firstDate.year == lastDate.year) {
                        _year.value = firstDate.yearMonth.year.toString()

                    } else {
                        val yearText = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                        _year.value = yearText
                    }
                }
            }
        }

    }

    private fun selectDate(day: CalendarDay) {
        _selectedDayHeader.value = dateFormatter.format(day.date)

        selectedDate = day.date
        calendar.notifyDayChanged(day)

        when (lastSelectedDay) {
            null -> calendar.notifyMonthChanged(day.date.yearMonth)
            else -> lastSelectedDay?.let { calendar.notifyDayChanged(it) }
        }

        lastSelectedDay = day

        updateAdapterForDate(day.date)
    }

    private fun updateAdapterForDate(date: LocalDate) {
        eventsAdapter.apply {
            events.clear()
            events.addAll(this@CalendarViewModel.events[date].orEmpty())
            notifyDataSetChanged()
        }
    }
}