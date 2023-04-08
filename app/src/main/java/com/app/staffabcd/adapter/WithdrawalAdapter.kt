package com.app.staffabcd.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.staffabcd.R
import com.app.staffabcd.model.Withdrawal

class WithdrawalAdapter (
    val activity: Activity,
    wallets: ArrayList<Withdrawal>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val withdrawals: ArrayList<Withdrawal>
    val activitys: Activity

    init {
        this.withdrawals = wallets
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.withdrawal_lyt, parent, false)
        return ItemHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder:ItemHolder = holderParent as ItemHolder
        val withdrawal: Withdrawal = withdrawals[position]
        holder.tvDateTime.text=withdrawal.datetime
        if (withdrawal.status=="0") {
            holder.tvStatus.text = "Pending"
            holder.tvStatus.setTextColor(activity.resources.getColor(R.color.primary))
        }
        else if (withdrawal.status=="paid") {
            holder.tvStatus.text = "Approved"
            holder.tvStatus.setTextColor(activity.resources.getColor(R.color.green))
        }
        else if (withdrawal.status=="2") {
            holder.tvStatus.text = "Rejected"
            holder.tvStatus.setTextColor(activity.resources.getColor(R.color.red))
        }
        holder.tvAmount.text="₹ "+withdrawal.amount


    }



    override fun getItemCount(): Int {
        return withdrawals.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDateTime: TextView
        val tvStatus: TextView
        val tvAmount: TextView


        init {
            tvDateTime = itemView.findViewById(R.id.tvDateTime)
            tvStatus = itemView.findViewById(R.id.tvStatus)
            tvAmount=itemView.findViewById(R.id.tvAmount)

        }
    }
}