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
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.app.staffabcd.R
import com.app.staffabcd.RemarksActivity
import com.app.staffabcd.model.Users


class MyLeadsAdapter(val activity: Activity, users: ArrayList<Users>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val users: ArrayList<Users>

    init {
        this.users = users
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.my_leads_lyt, parent, false)
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
        holder.tvTotalreferrals.text="No. of refers: "+users1.l_referral_count
        holder.btnChat.setOnClickListener {view ->
//            showPopupMenu(view,users1.mobile)

            // intent to open RemarksActivity

            val intent = Intent(activity, RemarksActivity::class.java)
            intent.putExtra("id",users1.id)
            intent.putExtra("remarks",users1.remarks)
            activity.startActivity(intent)

        }
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
        val btnChat: Button
        init {
            tvName = itemView.findViewById<TextView>(R.id.tvName)
            tvReferCode = itemView.findViewById<TextView>(R.id.tvReferCode)
            tvTotalcodes = itemView.findViewById<TextView>(R.id.tvTotalcodes)
            tvMobile = itemView.findViewById<TextView>(R.id.tvMobile)
            tvWokedDays = itemView.findViewById<TextView>(R.id.tvWokedDays)
            tvTotalreferrals = itemView.findViewById<TextView>(R.id.tvTotalreferrals)
            btnChat = itemView.findViewById<Button>(R.id.btnChat)

        }
    }
    private fun showPopupMenu(view: View, mobile: String?) {
        val popup = PopupMenu(activity, view)
        popup.inflate(R.menu.whatsapp_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.whatsapp -> sendMsgToWhatsapp(mobile,"com.whatsapp")
                R.id.business_whatsapp -> sendMsgToWhatsapp(mobile,"com.whatsapp.w4b")
            }
            true
        }
        popup.show()
    }
    private fun sendMsgToWhatsapp(mobile: String?, packageName: String) {

        val phoneNumber = "+91$mobile"
        val message = "Hi, How are you?" // Replace with the message you want to send
// Replace with the phone number you want to navigate to in WhatsApp

        val packageManager: PackageManager =
            activity.packageManager// Call packageManager on the activity instance
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url =
                "https://api.whatsapp.com/send?phone=$phoneNumber&text=$message" // Include the message in the URL

            i.setPackage(packageName)
            i.data = Uri.parse(url)

            activity.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }


    }

}