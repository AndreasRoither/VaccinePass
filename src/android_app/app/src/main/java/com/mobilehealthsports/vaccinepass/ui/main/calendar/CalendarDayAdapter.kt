package com.mobilehealthsports.vaccinepass.ui.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.databinding.ListItemVaccineEventBinding
import java.time.LocalDate

data class Event(val id: String, val text: String, val date: LocalDate)

class CalendarDayAdapter(val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<CalendarDayAdapter.CalendarEventsViewHolder>() {

    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarEventsViewHolder {
        // TODO: replace with correct vaccine layout from simon
        return CalendarEventsViewHolder(
            ListItemVaccineEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: CalendarEventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class CalendarEventsViewHolder(private val binding: ListItemVaccineEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) {
            binding.itemEventText.text = event.text
        }
    }
}