package com.app.staffabcd.fragments.reportFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.ReportAdapter
import com.app.staffabcd.databinding.FragmentLevelThreeBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Report
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LevelThreeFragment : Fragment() {
    lateinit var reportAdapter: ReportAdapter
    lateinit var binding: FragmentLevelThreeBinding
    lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLevelThreeBinding.inflate(inflater, container, false)
        session = Session(requireActivity())
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.levelThreeRecyclerView.layoutManager = linearLayoutManager
        reportThreeList()
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            reportThreeList()
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

    private fun reportThreeList() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.LEVEL] = "3"

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
                        reportAdapter = ReportAdapter(requireActivity(), reports, "3")
                        binding.levelThreeRecyclerView.setAdapter(reportAdapter)


                    }else{
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


}