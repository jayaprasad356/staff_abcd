package com.app.staffabcd.fragments.reportFragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.ReportAdapter
import com.app.staffabcd.databinding.FragmentLevelOneBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Report
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LevelOneFragment : Fragment() {
    lateinit var reportAdapter: ReportAdapter
    lateinit var binding: FragmentLevelOneBinding
    lateinit var session: Session

    val mobileNumbers: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLevelOneBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        binding.shareMobileNumber.setOnClickListener {

            shareMobileNumbersInWhatsApp(mobileNumbers)

        }

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.levelOneRecyclerView.layoutManager = linearLayoutManager
        reportOneList()
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            reportOneList()
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

    private fun reportOneList() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.LEVEL] = "1"
        }

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val reports: ArrayList<Report> = ArrayList()
                         // ArrayList to store mobile numbers

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                // Extract the values from the JSON object
                                val id = jsonObject1.getString(Constant.ID)
                                val name = jsonObject1.getString(Constant.NAME)
                                val referCode = jsonObject1.getString(Constant.REFER_CODE)
                                val totalCodes = jsonObject1.getString(Constant.TOTAL_CODES)
                                val workedDays = jsonObject1.getString(Constant.WORKED_DAYS)
                                val mobile = jsonObject1.getString(Constant.MOBILE)
                                val totalReferrals = jsonObject1.getString(Constant.L_REFERRAL_COUNT)

                                // Create a new Report object and add it to the list
                                val report = Report(id, name, referCode, totalCodes, workedDays, mobile, totalReferrals)
                                reports.add(report)

                                // Add the mobile number to the mobileNumbers ArrayList
                                mobileNumbers.add(mobile)
                            } else {
                                break
                            }
                        }

                        // Use the reports and mobileNumbers ArrayLists as needed
                        reportAdapter = ReportAdapter(requireActivity(), reports, "1")
                        binding.levelOneRecyclerView.adapter = reportAdapter

                        // Now the mobileNumbers ArrayList contains all the mobile numbers
                        // You can use it as needed, for example, print each mobile number
                        for (mobileNumber in mobileNumbers) {
                            Log.d("MobileNumber", mobileNumber)
                        }

                    } else {
                        Toast.makeText(
                            requireContext(), "No Data Found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.REPORTS_LIST, params, true)
    }


    private fun shareMobileNumbersInWhatsApp(mobileNumbers: ArrayList<String>) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        val mobileNumbersText = mobileNumbers.joinToString("\n") // Join the mobile numbers with line breaks
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, mobileNumbersText)

        // Create a chooser intent to let the user select WhatsApp or WhatsApp Business
        val chooserIntent = Intent.createChooser(whatsappIntent, "Share via")

        try {
            startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            // No app available to handle the intent
            Toast.makeText(requireContext(), "No app available for sharing", Toast.LENGTH_SHORT).show()
        }
    }


}