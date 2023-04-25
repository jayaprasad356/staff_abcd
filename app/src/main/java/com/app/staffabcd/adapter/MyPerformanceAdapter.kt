package com.app.staffabcd.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.staffabcd.R
import com.app.staffabcd.model.Performance


class MyPerformanceAdapter(val activity: Activity, performances: ArrayList<Performance>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val performances: ArrayList<Performance>

    init {
        this.performances = performances
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.my_performance_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val performance: Performance = performances[position]

        holder.tvdate.text = performance.date
        holder.tvTotalEarnings.text="Total Earnings : ₹"+performance.total_earn
        holder.tvTotalJoins.text="Total Joins: "+performance.total_joins
        holder.tvDirectJoins.text="Direct Joins: "+performance.direct_joins
    }

    override fun getItemCount(): Int {
        return performances.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvdate: TextView
        val tvTotalEarnings: TextView
        val tvTotalJoins: TextView
        val tvDirectJoins: TextView
        init {
            tvdate = itemView.findViewById<TextView>(R.id.tvDate)
            tvTotalEarnings = itemView.findViewById<TextView>(R.id.tvTotalEarning)
            tvTotalJoins = itemView.findViewById<TextView>(R.id.tvTotalJoins)
            tvDirectJoins = itemView.findViewById<TextView>(R.id.tvDirectJoins)
        }
    }
}