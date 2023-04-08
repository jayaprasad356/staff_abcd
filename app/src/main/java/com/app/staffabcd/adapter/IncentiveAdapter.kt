package com.app.staffabcd.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.staffabcd.R
import com.app.staffabcd.model.Incentive


class IncentiveAdapter(val activity: Activity, wallets: ArrayList<Incentive>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val reports: ArrayList<Incentive>

    init {
        this.reports = wallets
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.incentive_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder:ItemHolder = holderParent as ItemHolder
        val report: Incentive = reports[position]
        holder.date.text=report.date
        holder.earning.text="₹ "+report.incentives
        holder.lead.text=report.total_leads
        holder.joining.text=report.total_joinings


    }

    override fun getItemCount(): Int {
        return reports.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView
        val earning: TextView
        val lead: TextView
        val joining: TextView


        init {
            date = itemView.findViewById<TextView>(R.id.tvDate)
            earning = itemView.findViewById<TextView>(R.id.tvEarning)
            joining = itemView.findViewById<TextView>(R.id.tvJoining)
            lead = itemView.findViewById<TextView>(R.id.tvLead)

        }
    }
}