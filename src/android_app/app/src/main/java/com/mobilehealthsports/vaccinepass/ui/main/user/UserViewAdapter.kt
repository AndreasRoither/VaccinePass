package com.mobilehealthsports.vaccinepass.ui.main.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ListItemHeaderBinding
import com.mobilehealthsports.vaccinepass.databinding.ListItemVaccineBinding
import com.mobilehealthsports.vaccinepass.ui.main.calendar.Event
import java.lang.IllegalStateException




class VaccineViewAdapter(val onClick: (Event) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items : MutableList<ListItem> = mutableListOf()

    // This is your ViewHolder class that helps to populate data to the view
    inner class VaccineViewHolder(private val binding: ListItemVaccineBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vaccineItem: VaccineItem) {
            binding.item = vaccineItem
            //binding.executePendingBindings()
        }
    }

    inner class HeaderViewHolder(private val binding: ListItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: HeaderItem) {
            binding.item = header
            //binding.executePendingBindings()
        }
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflate the layout view you have created for the list rows here
        return when (viewType) {
            ListItemType.HEADER.ordinal -> {
                val binding: ListItemHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_header, parent, false)
                HeaderViewHolder(binding)
            }
            ListItemType.VACCINE.ordinal -> {
                val binding: ListItemVaccineBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_vaccine, parent, false)
                VaccineViewHolder(binding)
            }
            else -> throw IllegalStateException("unsupported item type")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val listItem = items[position]
        when (listItem.type) {
            ListItemType.VACCINE -> {
                val vaccineItemItem: VaccineItem = listItem as VaccineItem
                val holder: VaccineViewHolder = viewHolder as VaccineViewHolder
                holder.bind(vaccineItemItem)
            }
            ListItemType.HEADER -> {
                val headerItem: HeaderItem = listItem as HeaderItem
                val holder: HeaderViewHolder = viewHolder as HeaderViewHolder
                holder.bind(headerItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Not sure if following would be better
        //  return when(listItems[position].type){
        //            ListItemType.HEADERTYPE -> 0
        //            ListItemType.VACCINETYPE -> 1
        //  }
        // should however give the same results
        return items[position].type.ordinal
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}
