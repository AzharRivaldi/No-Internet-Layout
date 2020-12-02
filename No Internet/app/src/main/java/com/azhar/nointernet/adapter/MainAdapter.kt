package com.azhar.nointernet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azhar.nointernet.R
import com.azhar.nointernet.adapter.MainAdapter.MainHolder
import com.azhar.nointernet.model.ModelMain
import kotlinx.android.synthetic.main.list_item_main.view.*

/**
 * Created by Azhar Rivaldi on 01-12-2020
 */

class MainAdapter(private val items: List<ModelMain>) : RecyclerView.Adapter<MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_main, parent, false)
        return MainHolder(view)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val data = items[position]
        holder.tvName.text = data.strName
        holder.tvVersion.text = data.strVersion
        holder.tvApiLevel.text = data.strVersion
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvVersion: TextView
        var tvApiLevel: TextView

        init {
            tvName = itemView.tvName
            tvVersion = itemView.tvVersion
            tvApiLevel = itemView.tvApiLevel
        }
    }
}