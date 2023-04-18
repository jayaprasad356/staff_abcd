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
        holder.tvName.text=report.name
        holder.tvJoiningDate.text=report.joined_date
        holder.tvType.text=report.type
        holder.tvAmount.text="₹ "+report.amount
        holder.tvReferCode.text=report.refer_code


    }

    override fun getItemCount(): Int {
        return reports.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvJoiningDate: TextView
        val tvType: TextView
        val tvAmount: TextView
        val tvReferCode: TextView


        init {
            tvName = itemView.findViewById<TextView>(R.id.tvName)
            tvJoiningDate = itemView.findViewById<TextView>(R.id.tvJoinDate)
            tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
            tvType = itemView.findViewById<TextView>(R.id.tvType)
            tvReferCode = itemView.findViewById<TextView>(R.id.tvReferCode)

        }
    }
}