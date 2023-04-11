package com.app.staffabcd.fragments.reportFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.HistoryAdapter
import com.app.staffabcd.adapter.ReportAdapter
import com.app.staffabcd.databinding.FragmentHistoryBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Report
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class HistoryFragment : Fragment() {

    lateinit var historyAdapter: HistoryAdapter
    lateinit var binding: FragmentHistoryBinding
    lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        session= Session(requireActivity())
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.historyRecyclerView.layoutManager = linearLayoutManager
        historyList()
        return binding.root
    }

    private fun historyList() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] =  session.getData(Constant.STAFF_ID)
            this[Constant.LEVEL] =  "2"

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val reports: ArrayList<Report> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                // Extract the values from the JSON object
                                val id = jsonObject1.getString(Constant.ID)
                                val name = jsonObject1.getString(Constant.NAME)
                                val mobile = jsonObject1.getString(Constant.MOBILE)
                                val date = jsonObject1.getString(Constant.JOINED_DATE)
                                val level = jsonObject1.getString(Constant.LEVEL)
                                val historyDay = jsonObject1.getString(Constant.HISTORY_DAYS)

                                if (historyDay >= "3" && level.equals("2")){
                                    // Create a new Report object and add it to the list
                                    val report = Report(id,name, mobile,date,level,historyDay)
                                    reports.add(report)
                                }

                            } else {
                                break
                            }
                        }
                        historyAdapter = HistoryAdapter(requireActivity(), reports)
                        binding.historyRecyclerView.setAdapter(historyAdapter)


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFF_REPORTS, params, true)
    }
}