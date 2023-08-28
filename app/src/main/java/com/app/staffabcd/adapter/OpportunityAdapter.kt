package com.app.staffabcd.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.app.staffabcd.HomeActivity
import com.app.staffabcd.MainActivity

import com.app.staffabcd.R
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Opportunity
import com.app.staffabcd.model.Users
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class OpportunityAdapter(val activity: Activity, opportunity: ArrayList<Opportunity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val opportunity: ArrayList<Opportunity>
    val session = Session(activity)

    init {
        this.opportunity = opportunity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.opportunity_lyt, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val users1: Opportunity = opportunity[position]

        var id = users1.id;

        holder.tvName.text = users1.name
        holder.tvReferCode.text="Refer Codes: "+users1.refer_code
        holder.tvTotalcodes.text="Total Codes: "+users1.total_codes
        holder.tvMobile.text=users1.mobile
        holder.tvWokedDays.text="Worked Days: "+users1.worked_days
        holder.tvTotalreferrals.text="No. of refers: "+users1.l_referral_count
        holder.btnChat.setOnClickListener {view ->

            showConfirmationDialog(id)


        }
    }



    override fun getItemCount(): Int {
        return opportunity.size
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



    private fun showConfirmationDialog( id: String?) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure take the user?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
            // Perform action when "Yes" is clicked
            // Add your code here
            apicall(id)
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            // Perform action when "No" is clicked
            dialogInterface.dismiss()
            // Add your code here
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun apicall( id: String?) {

        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] =  session.getData(Constant.STAFF_ID)
            this[Constant.USER_ID] = id.toString()

//            Log.d("take",session.getData(Constant.STAFF_ID) + " " + id.toString()   )
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        val Intent = Intent(activity,HomeActivity::class.java)
                        activity.startActivity(Intent)

                        Toast.makeText(activity,"User Taken Successfully", Toast.LENGTH_SHORT).show()


                    }else{
                        Toast.makeText(activity,"No Data Found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.TAKE_USER, params, true)
    }


}