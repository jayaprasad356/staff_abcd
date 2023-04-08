package com.app.staffabcd.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.staffabcd.R
import com.app.staffabcd.model.Transanction


class TransactionAdapter(val activity: Activity, wallets: ArrayList<Transanction>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val wallets: ArrayList<Transanction>

    init {
        this.wallets = wallets
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.transaction_list_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val transanction: Transanction = wallets[position]
        holder.tvTitle.text = transanction.type

        holder.tvDateTime.setText(transanction.date).toString()
        holder.tvAmount.setText("+ ₹" + transanction.amount)
    }

    override fun getItemCount(): Int {
        return wallets.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val tvDateTime: TextView
        val tvAmount: TextView

        init {
            tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            tvDateTime = itemView.findViewById<TextView>(R.id.tvDateTime)
            tvAmount = itemView.findViewById<TextView>(R.id.tvAmount)
        }
    }
}