package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ListItemScheduleBinding

class ScheduleViewAdapter(private val items: List<ScheduleItem> ) :  RecyclerView.Adapter<ScheduleViewAdapter.ScheduleViewHolder>() {

    // This is your ViewHolder class that helps to populate data to the view
    inner class ScheduleViewHolder(private val binding: ListItemScheduleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: ScheduleItem) {
            binding.item = schedule
        }
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemScheduleBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_schedule, parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

}