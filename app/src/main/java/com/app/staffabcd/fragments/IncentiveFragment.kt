package com.app.staffabcd.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.IncentiveAdapter
import com.app.staffabcd.databinding.FragmentIncentiveBinding
import com.app.staffabcd.model.Incentive

class IncentiveFragment : Fragment() {

    lateinit var incentiveAdapter: IncentiveAdapter
    lateinit var binding: FragmentIncentiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncentiveBinding.inflate(inflater, container, false)

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvReport.layoutManager = linearLayoutManager
        IncentiveList()
        return binding.root
    }
    private fun IncentiveList() {
// Create a list of Report objects with dummy data
        val reports = listOf(
            Incentive("2022-01-01", "$1000", "John ", "5"),
            Incentive("2022-01-02", "$2000", "Jane ", "8"),
            Incentive("2022-01-03", "$1500", "Bob ", "3"),
            Incentive("2022-01-04", "$3000", "Sarah ", "10"),
            Incentive("2022-01-05", "$500", "David ", "2")
        )
        val reportsArrayList = ArrayList(reports)


        incentiveAdapter = IncentiveAdapter(requireActivity(), reportsArrayList)
        binding.rvReport.setAdapter(incentiveAdapter)

//
//        val params : HashMap<String,String> = hashMapOf()
//        params.apply {
//            this["user_id"] =  "23319"
//        }
//        ApiConfig.RequestToVolley({ result, response ->
//            if (result) {
//                try {
//                    val jsonObject = JSONObject(response)
//                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
//                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
//                        val reports: ArrayList<Report> = ArrayList<Report>()
//                        Report("2022-01-01", "$1000", "John Doe", "5")
//                        Report("2022-01-02", "$2000", "Jane Smith", "8")
//                        Report("2022-01-03", "$1500", "Bob Johnson", "3")
//                        Report("2022-01-04", "$3000", "Sarah Lee", "10")
//                        Report("2022-01-05", "$500", "David Kim", "2")
//                                              val g = Gson()
//                        for (i in 0 until jsonArray.length()) {
//                            val jsonObject1 = jsonArray.getJSONObject(i)
//                            if (jsonObject1 != null) {
//                                val group: Report =
//                                    g.fromJson(jsonObject1.toString(), Report::class.java)
//                                reports.add(group)
//                            } else {
//                                break
//                            }
//                        }
//                        reportAdapter = ReportAdapter(requireActivity(), reports)
//                        binding.rvReport.setAdapter(reportAdapter)
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }
//        }, requireActivity(), "https://abcd.graymatterworks.com/api/"+ Constant.TRNSACTION_LIST_URL, params, true)
    }

}