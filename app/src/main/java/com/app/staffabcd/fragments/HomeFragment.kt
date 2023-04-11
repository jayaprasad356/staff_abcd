package com.app.staffabcd.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.HomeActivity
import com.app.staffabcd.R
import com.app.staffabcd.adapter.IncentiveAdapter
import com.app.staffabcd.adapter.IncentivesAdapter
import com.app.staffabcd.databinding.FragmentDocumentsBinding
import com.app.staffabcd.databinding.FragmentHomeBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Incentive
import com.app.staffabcd.model.Incentives
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var IncentivesAdapter: IncentivesAdapter
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        initCall()



        binding.cvJoining.setOnClickListener {
            navigateToReport()
        }
        binding.cvIncentive.setOnClickListener {
            navigateToReport()
        }
        binding.cvLead.setOnClickListener {
            navigateToReport()
        }
        binding.cvSalary.setOnClickListener {
            showMoveSalaryDialog()
        }
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvIncentives.layoutManager = linearLayoutManager

        IncentivesList()

        return binding.root
    }

    private fun initCall() {
        binding.tvName.text = session.getData(Constant.FIRST_NAME)
        binding.tvMobil.text = session.getData(Constant.MOBILE)
        binding.tvStaffId.text = session.getData(Constant.STAFF_DISPLAY_ID)
        binding.tvwalletBalance.text = "₹ " + session.getData(Constant.BALANCE)
        binding.tvTotalEarning.text="₹ " + session.getData(Constant.TOTAL_EARNINGS)
        binding.tvIncentiveEarn.text="₹ " + session.getData(Constant.INCENTIVE_EARN)
        binding.tvTotalLead.text=session.getData(Constant.TOTAL_LEADS)
        binding.tvTotalJoining.text= session.getData(Constant.TOTAL_JOININGS)

    }

    private fun navigateToReport() {
        val reportFragment = ReportFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FrameLyt, reportFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showMoveSalaryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        val dialogView = inflater.inflate(R.layout.dialog_move_salary, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            // Perform action to move salary to wallet
            dialog.dismiss()
        }
    }

    private fun IncentivesList() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] =  session.getData(Constant.STAFF_ID)
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val incentive: ArrayList<Incentives> = ArrayList<Incentives>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Incentives =
                                    g.fromJson(jsonObject1.toString(), Incentives::class.java)
                                incentive.add(group)
                            } else {
                                break
                            }
                        }
                        val sortedIncentives = incentive.sortedBy { it.id }
                        incentive.clear()
                        incentive.addAll(sortedIncentives)
                        IncentivesAdapter = IncentivesAdapter(requireActivity(), incentive)
                        binding.rvIncentives.setAdapter(IncentivesAdapter)

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFF_TOPEARNERS, params, true)
    }
//    {
//// Create a list of Report objects with dummy data
//        val reports = listOf(
//            Incentives("1","Ajay", "1000"),
//            Incentives("2","Ajay", "1000"),
//            Incentives("3","Ajay", "1500"),
//            Incentives("4","Ajay", "3000"),
//            Incentives("5","Ajay", "500")
//        )
//        val reportsArrayList = ArrayList(reports)
//
//
//        IncentivesAdapter = IncentivesAdapter(requireActivity(), reportsArrayList)
//        binding.rvIncentives.setAdapter(IncentivesAdapter)
//
////
////        val params : HashMap<String,String> = hashMapOf()
////        params.apply {
////            this["user_id"] =  "23319"
////        }
////        ApiConfig.RequestToVolley({ result, response ->
////            if (result) {
////                try {
////                    val jsonObject = JSONObject(response)
////                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
////                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
////                        val reports: ArrayList<Report> = ArrayList<Report>()
////                        Report("2022-01-01", "$1000", "John Doe", "5")
////                        Report("2022-01-02", "$2000", "Jane Smith", "8")
////                        Report("2022-01-03", "$1500", "Bob Johnson", "3")
////                        Report("2022-01-04", "$3000", "Sarah Lee", "10")
////                        Report("2022-01-05", "$500", "David Kim", "2")
////                                              val g = Gson()
////                        for (i in 0 until jsonArray.length()) {
////                            val jsonObject1 = jsonArray.getJSONObject(i)
////                            if (jsonObject1 != null) {
////                                val group: Report =
////                                    g.fromJson(jsonObject1.toString(), Report::class.java)
////                                reports.add(group)
////                            } else {
////                                break
////                            }
////                        }
////                        reportAdapter = ReportAdapter(requireActivity(), reports)
////                        binding.rvReport.setAdapter(reportAdapter)
////                    }
////                } catch (e: JSONException) {
////                    e.printStackTrace()
////                }
////            }
////        }, requireActivity(), "https://abcd.graymatterworks.com/api/"+ Constant.TRNSACTION_LIST_URL, params, true)
//    }


}