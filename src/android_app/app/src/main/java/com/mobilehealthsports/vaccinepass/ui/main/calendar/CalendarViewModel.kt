package com.mobilehealthsports.vaccinepass.ui.main.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.repository.AppointmentRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.ui.main.adapter.ItemViewAdapter
import com.mobilehealthsports.vaccinepass.ui.main.user.AppointmentItem
import com.mobilehealthsports.vaccinepass.util.livedata.NonNullMutableLiveData
import com.mobilehealthsports.vaccinepass.util.daysOfWeekFromLocale
import com.mobilehealthsports.vaccinepass.util.setTextColorRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarViewModel(private val appointmentRepository: AppointmentRepository) :
    BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()
    val navigationRequest = ServiceRequest<NavigationRequest>()

    public var selectedDate = LocalDate.now()
    private var lastSelectedDay: CalendarDay? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.YY")
    private var events = mutableMapOf<LocalDate, MutableList<AppointmentItem>>()
    private lateinit var calendar: com.kizitonwose.calendarview.CalendarView
    val addEntry = NonNullMutableLiveData(false)
    val selectedId = MutableLiveData(-1L)

    private var _month = MutableLiveData(monthTitleFormatter.format(LocalDate.now().yearMonth))
    var month: LiveData<String> = _month

    private var _year = MutableLiveData(LocalDate.now().yearMonth.year.toString())
    var year: LiveData<String> = _year

    private var _selectedDayHeader = MutableLiveData(dateFormatter.format(LocalDate.now()))
    var selectedDayHeader: LiveData<String> = _selectedDayHeader

    val eventsAdapter = ItemViewAdapter {
        messageRequest.request(ToastRequest("Clicked " + it.text))
    }

    fun setupCalendar(calendar: com.kizitonwose.calendarview.CalendarView) {
        // calendar
        this.calendar = calendar
        val daysOfWeek = daysOfWeekFromLocale()

        viewModelScope.launch(Dispatchers.IO) {
            eventsAdapter.apply {
                val appointments = appointmentRepository.getAllAppointments().map {
                    AppointmentItem(it.uid, it.title!!, it.appointment_date!!, it.reminder, this@CalendarViewModel::onAppointmentItemClick);
                }
                events = appointments.groupByTo(mutableMapOf()) { it.appointment_date }
            }
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
                            textView.setTextColorRes(R.color.colorPrimary)
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
        //check for initial date if there are any appointments
        updateAdapterForDate(LocalDate.now());

    }

    private fun onAppointmentItemClick(id: Long) {
        selectedId.value = id
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
            this.items.clear()
            val appointments: List<AppointmentItem> = this@CalendarViewModel.events[date].orEmpty()
            this.items = appointments.toMutableList()
            notifyDataSetChanged()
        }
    }

    public fun addEntry() {
        addEntry.value = true;
    }
}