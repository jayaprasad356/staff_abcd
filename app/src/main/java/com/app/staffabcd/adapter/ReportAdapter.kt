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
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Report
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ReportAdapter(
    val activity: Activity,
    wallets: ArrayList<Report>,
    var level: String,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val reports: ArrayList<Report>
    val activitys: Activity
    var session: Session

    init {
        this.reports = wallets
        this.activitys = activity
        session = Session(activity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.report_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: Report = reports[position]
        holder.name.text = report.name
        holder.tvReferCode.text = report.refer_code
        holder.tvMobile.text=report.mobile
        holder.btnChat.setOnClickListener {
            sendMsgToWhatsapp(report.mobile)
            // sendMessage("Hello, this is a test message.")
        }

    }

    private fun sendMsgToWhatsapp(mobile: String?) {

        val phoneNumber = "+91$mobile"
        val message = "Hello, this is a test message." // Replace with the message you want to send
// Replace with the phone number you want to navigate to in WhatsApp

        val packageManager: PackageManager =
            activity.packageManager// Call packageManager on the activity instance
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url =
                "https://api.whatsapp.com/send?phone=$phoneNumber&text=$message" // Include the message in the URL

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
        val tvReferCode: TextView
        val btnChat: Button
        val tvMobile: TextView


        init {
            name = itemView.findViewById(R.id.tvName)
            tvReferCode = itemView.findViewById(R.id.tvReferCode)
            tvMobile = itemView.findViewById(R.id.tvMobile)
            btnChat = itemView.findViewById(R.id.btnChat)

        }
    }

    private fun markMessaged(level: String, id: String) {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.LEVEL] = level
            this[Constant.ID] = id


        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(
                            activity,
                            jsonObject.getString(Constant.MESSAGE),
                            Toast.LENGTH_SHORT
                        ).show()
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val reports: ArrayList<Report> = ArrayList()


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.REPORTS_LIST, params, true)
    }

}