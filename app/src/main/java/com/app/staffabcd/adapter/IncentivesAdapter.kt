package com.app.staffabcd.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.staffabcd.R
import com.app.staffabcd.model.Incentives

class IncentivesAdapter (
    val activity: Activity,
    incentives: ArrayList<Incentives>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val incentives: ArrayList<Incentives>
    val activitys: Activity

    init {
        this.incentives = incentives
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.incentives_lyt, parent, false)
        return ItemHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder:ItemHolder = holderParent as ItemHolder
        val incentives: Incentives = incentives[position]
        holder.tvName.text=incentives.name

        holder.tvAmount.text="₹ "+incentives.amount
    }



    override fun getItemCount(): Int {
        return incentives.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvAmount: TextView


        init {
            tvName = itemView.findViewById(R.id.tvName)
            tvAmount=itemView.findViewById(R.id.tvAmount)

        }
    }
}