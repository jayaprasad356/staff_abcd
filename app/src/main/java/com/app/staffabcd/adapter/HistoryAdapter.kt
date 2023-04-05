package com.app.staffabcd.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.staffabcd.R
import com.app.staffabcd.model.Report

class HistoryAdapter (
    val activity: Activity,
    wallets: ArrayList<Report>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val reports: ArrayList<Report>
    val activitys: Activity

    init {
        this.reports = wallets
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.history_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder:ItemHolder = holderParent as ItemHolder
        val report: Report = reports[position]
        holder.name.text=report.name
        holder.joining.text=report.joining
        holder.btnMsg.setOnClickListener {
            sendMsgToWhatsapp(report.mobile)
        }

    }
    private fun sendMsgToWhatsapp(mobile: String?) {

        val phoneNumber = "+91$mobile"
        val message = "Hello, this is a test message." // Replace with the message you want to send
// Replace with the phone number you want to navigate to in WhatsApp

        val packageManager : PackageManager =activity.packageManager// Call packageManager on the activity instance
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=$message" // Include the message in the URL

            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)

            activity.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }


    }



    override fun getItemCount(): Int {
        return reports.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val joining: TextView
        val btnMsg: Button


        init {
            name = itemView.findViewById(R.id.tvName)
            joining = itemView.findViewById(R.id.tvJoining)
            btnMsg=itemView.findViewById(R.id.btnMsg)

        }
    }
}