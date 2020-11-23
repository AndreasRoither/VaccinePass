package com.mobilehealthsports.vaccinepass.ui.pin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R

/**
 * Custom adapter for pin view in PinActivity
 */
class PinViewAdapter(private val pins: List<Int>) : RecyclerView.Adapter<PinViewAdapter.ViewHolder>() {

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflate the layout view you have created for the list rows here
        val view: View = layoutInflater.inflate(R.layout.list_item_circle, parent, false)
        return ViewHolder(view)
    }


    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pin: Int) {
            // if pins should change their data -> this would be the place
        }
    }

    override fun getItemCount(): Int {
        return pins.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pin = pins[position]
        holder.bind(pin)
    }
}