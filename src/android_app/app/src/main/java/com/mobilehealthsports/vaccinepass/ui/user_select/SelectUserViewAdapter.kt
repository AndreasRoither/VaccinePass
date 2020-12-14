package com.mobilehealthsports.vaccinepass.ui.user_select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.databinding.ListItemUserBinding

class SelectUserViewAdapter(private val users: List<User>, private val clickListener: SelectUserViewModel.ItemClickListener) : RecyclerView.Adapter<SelectUserViewAdapter.ViewHolder>() {

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflate the layout view you have created for the list rows here
        val binding : ListItemUserBinding = DataBindingUtil.inflate(layoutInflater,R.layout.list_item_user, parent, false)


        return ViewHolder(binding)
    }




    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(private val binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, clickListener: SelectUserViewModel.ItemClickListener) {
            binding.item = user
            binding.itemClick = clickListener
        }
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, clickListener)
    }
}