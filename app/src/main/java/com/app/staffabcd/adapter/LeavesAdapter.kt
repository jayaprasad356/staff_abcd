package com.app.staffabcd.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.staffabcd.R
import com.app.staffabcd.model.Leaves


class LeavesAdapter(val activity: Activity, val leaves: ArrayList<Leaves>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(activity).inflate(R.layout.leave_list_lyt, parent, false)
        return ExploreItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder = holderParent as ExploreItemHolder
        val leave = leaves[position]
        holder.tvDate.setText(leave.date)
        holder.tvReason.setText(leave.reason)
        if (leave.status.equals("1")) {
            holder.tvStatus.text = "Approved"
            holder.tvStatus.setTextColor(Color.GREEN)
        } else if (leave.status.equals("2")) {
            holder.tvStatus.text = "Not Approved"
            holder.tvStatus.setTextColor(Color.RED)
        } else {
            holder.tvStatus.text = "Pending"
            holder.tvStatus.setTextColor(Color.BLUE)
        }
    }

    override fun getItemCount(): Int {
        return leaves.size
    }

    internal class ExploreItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView
        val tvReason: TextView
        val tvStatus: TextView

        init {
            tvDate = itemView.findViewById(R.id.tvDate)
            tvReason = itemView.findViewById(R.id.tvReason)
            tvStatus = itemView.findViewById(R.id.tvStatus)
        }
    }
}

