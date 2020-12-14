package com.mobilehealthsports.vaccinepass.ui.main.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.ListItemSettingsBinding

class SettingsViewAdapter(private val listItems: List<SettingsItem>): RecyclerView.Adapter<SettingsViewAdapter.SettingsViewHolder>()  {

    inner class SettingsViewHolder(private val binding: ListItemSettingsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemSettingsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_settings, parent ,false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val listItem = listItems[position]
        holder.bind(listItem)
    }

    override fun getItemCount(): Int {
        return listItems.count()
    }
}