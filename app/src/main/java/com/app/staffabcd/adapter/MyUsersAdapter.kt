package com.app.staffabcd.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.staffabcd.R
import com.app.staffabcd.model.Users


class MyUsersAdapter(val activity: Activity, users: ArrayList<Users>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val users: ArrayList<Users>

    init {
        this.users = users
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.my_users_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val users1: Users = users[position]

        holder.tvName.text = users1.name
        holder.tvReferCode.text=users1.refer_code
        holder.tvTotalcodes.text="Total Codes: "+users1.total_codes
        holder.tvMobile.text=users1.mobile
        holder.tvWokedDays.text="Worked Days: "+users1.worked_days
        holder.tvTotalreferrals.text="No. of refers: "+users1.total_referrals
        holder.tvTotalreferrals.text="No. of refers: "+users1.total_referrals
    }

    override fun getItemCount(): Int {
        return users.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvReferCode: TextView
        val tvTotalcodes: TextView
        val tvMobile: TextView
        val tvWokedDays: TextView
        val tvTotalreferrals: TextView
        init {
            tvName = itemView.findViewById<TextView>(R.id.tvName)
            tvReferCode = itemView.findViewById<TextView>(R.id.tvReferCode)
            tvTotalcodes = itemView.findViewById<TextView>(R.id.tvTotalcodes)
            tvMobile = itemView.findViewById<TextView>(R.id.tvMobile)
            tvWokedDays = itemView.findViewById<TextView>(R.id.tvWokedDays)
            tvTotalreferrals = itemView.findViewById<TextView>(R.id.tvTotalreferrals)
        }
    }
}