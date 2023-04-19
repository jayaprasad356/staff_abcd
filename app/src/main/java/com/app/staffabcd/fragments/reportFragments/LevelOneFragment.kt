package com.app.staffabcd.fragments.reportFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLevelOneBinding.inflate(inflater, container, false)
        session = Session(requireActivity())
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.levelOneRecyclerView.layoutManager = linearLayoutManager
        reportOneList()
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
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                // Extract the values from the JSON object
                                val id = jsonObject1.getString(Constant.ID)
                                val name = jsonObject1.getString(Constant.NAME)
                                val mobile = jsonObject1.getString(Constant.MOBILE)
                                val date = jsonObject1.getString(Constant.JOINED_DATE)


                                    // Create a new Report object and add it to the list
                                    val report = Report(id, name, mobile, date)
                                    reports.add(report)


                            } else {
                                break
                            }
                        }
                        reportAdapter = ReportAdapter(requireActivity(), reports,"1")
                        binding.levelOneRecyclerView.setAdapter(reportAdapter)


                    }else{
                        val reports: ArrayList<Report> = ArrayList()

                        val report1 = Report("1", "John Doe", "1234567890", "2022-01-01")
                        val report2 = Report("2", "Jane Smith", "0987654321", "2022-02-01")
                        reports.add(report1)
                        reports.add(report2)
                        reportAdapter = ReportAdapter(requireActivity(), reports,"1")
                        binding.levelOneRecyclerView.adapter = reportAdapter

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.REPORTS_LIST, params, true)
    }

}