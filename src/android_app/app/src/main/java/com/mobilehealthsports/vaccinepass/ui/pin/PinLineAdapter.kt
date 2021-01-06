package com.mobilehealthsports.vaccinepass.ui.pin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R

class PinLineViewAdapter(private val pins: List<Int>) : RecyclerView.Adapter<PinLineViewAdapter.ViewHolder>() {

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
        fun bind() {}
    }

    override fun getItemCount(): Int {
        return pins.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
}