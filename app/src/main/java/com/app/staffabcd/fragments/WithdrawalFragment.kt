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
import com.app.staffabcd.adapter.WithdrawalAdapter
import com.app.staffabcd.databinding.FragmentWithdrawalBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Incentive
import com.app.staffabcd.model.Withdrawal
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class WithdrawalFragment : Fragment() {

    lateinit var binding: FragmentWithdrawalBinding
    lateinit var btnWithdaw: Button
    lateinit var activity: Activity
    lateinit var withdrawalAdapter: WithdrawalAdapter
    lateinit var session: Session


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        binding = FragmentWithdrawalBinding.inflate(inflater, container, false)
        session=Session(requireActivity())
        btnWithdaw = binding.btnWithdraw
        activity = requireActivity()
        btnWithdaw.setOnClickListener {
            val bankDetailFragment = BankDetailFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.FrameLyt, bankDetailFragment)
            transaction.commit()
        }
        binding.tvwalletBalance.text=session.getData(Constant.BALANCE).toString()

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvWithdrawalHistory.layoutManager = linearLayoutManager
        withdrawalList()
        return binding.root
    }

    private fun withdrawalList() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val withdrawalArrayList: ArrayList<Withdrawal> = ArrayList<Withdrawal>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Withdrawal =
                                    g.fromJson(jsonObject1.toString(), Withdrawal::class.java)
                                withdrawalArrayList.add(group)
                            } else {
                                break
                            }
                        }
                        withdrawalAdapter = WithdrawalAdapter(requireActivity(), withdrawalArrayList)
                        binding.rvWithdrawalHistory.adapter = withdrawalAdapter


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFFS_WITHDRAWALS_LIST, params, true)
    }

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