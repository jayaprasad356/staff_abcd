package com.app.staffabcd.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager


import com.app.staffabcd.R
import com.app.staffabcd.adapter.ReportAdapter
import com.app.staffabcd.adapter.WithdrawalAdapter
import com.app.staffabcd.databinding.FragmentWithdrawalBinding
import com.app.staffabcd.model.Withdrawal


class WithdrawalFragment : Fragment() {

lateinit var binding: FragmentWithdrawalBinding
lateinit var btnWithdaw: Button
lateinit var activity:Activity
lateinit var withdrawalAdapter: WithdrawalAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        binding = FragmentWithdrawalBinding.inflate(inflater, container, false)
        btnWithdaw=binding.btnWithdraw
        activity= requireActivity()
        btnWithdaw.setOnClickListener{
            val bankDetailFragment = BankDetailFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.FrameLyt, bankDetailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvWithdrawalHistory.layoutManager = linearLayoutManager
        withdrawalList()
        return binding.root
    }
    private fun withdrawalList() {
        val reports = listOf(
            Withdrawal("0", "52", "12/02/2022"),
            Withdrawal("1", "456", "12/02/2022"),
            Withdrawal("2", "52", "12/02/2022"),
            Withdrawal("0", "154", "12/02/2022"),
        )
        val withdrawalArraylist = ArrayList(reports)


        withdrawalAdapter = WithdrawalAdapter(requireActivity(), withdrawalArraylist)
        binding.rvWithdrawalHistory.setAdapter(withdrawalAdapter)

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