package com.mobilehealthsports.vaccinepass.ui.main
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R

class VaccineViewAdapter(private val vaccines: List<Vaccine>) : RecyclerView.Adapter<VaccineViewAdapter.ViewHolder>() {
    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflate the layout view you have created for the list rows here
        val view: View = layoutInflater.inflate(R.layout.list_item_vaccine, parent, false)
        return ViewHolder(view)
    }


    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vaccine: Vaccine) {
            // if pins should change their data -> this would be the place
        }
    }

    override fun getItemCount(): Int {
        return vaccines.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccine = vaccines[position]
        holder.bind(vaccine)
    }
}