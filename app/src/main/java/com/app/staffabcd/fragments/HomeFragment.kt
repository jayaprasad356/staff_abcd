package com.app.staffabcd.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.R
import com.app.staffabcd.adapter.IncentivesAdapter
import com.app.staffabcd.databinding.FragmentHomeBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Incentives
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var incentivesAdapter: IncentivesAdapter
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            navigateToWithdrawals()
        }
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvIncentives.layoutManager = linearLayoutManager

        incentivesList()
        staffDetails()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initCall() {
        binding.tvName.text = session.getData(Constant.NAME)
        binding.tvMobil.text = session.getData(Constant.MOBILE)
        binding.tvStaffId.text = session.getData(Constant.STAFF_DISPLAY_ID)
        binding.tvwalletBalance.text = "₹ " + session.getData(Constant.BALANCE)
        binding.tvTotalEarning.text="₹ " + session.getData(Constant.TOTAL_EARNINGS)
        binding.tvSalary.text="₹ " + session.getData(Constant.SALARY)
        binding.tvRole.text=session.getData(Constant.ROLE)

        var incentiveEarn = session.getData(Constant.INCENTIVE_EARN)
        if (session.getData(Constant.INCENTIVE_EARN).equals("null")){
             incentiveEarn = "0"
        }

        binding.tvIncentiveEarn.text= "₹ $incentiveEarn"
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
    private fun navigateToWithdrawals() {
        val withdrawalFragment = WithdrawalFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FrameLyt, withdrawalFragment)
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

    private fun incentivesList() {
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
                        val incentive: ArrayList<Incentives> = ArrayList()
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
//                        val sortedIncentives = incentive.sortedBy { it.id }
//                        incentive.clear()
//                        incentive.addAll(sortedIncentives)
                        incentivesAdapter = IncentivesAdapter(requireActivity(), incentive)
                        binding.rvIncentives.adapter = incentivesAdapter

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFF_TOPEARNERS, params, true)
    }
    private fun staffDetails() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

//                        val message = jsonObject.getString("message")
//                        val documentUpload = jsonObject.getInt("document_upload")
//                        val salary = jsonObject.getString("salary")
//                        val incentiveEarn = jsonObject.getString("incentive_earn")
//                        val totalEarnings = jsonObject.getInt("total_earnings")
//                        val totalLeads = jsonObject.getString("total_leads")
//                        val totalJoinings = jsonObject.getString("total_joinings")


                        val userData: JSONObject =
                            jsonObject.getJSONArray(Constant.DATA).getJSONObject(0)
                        session.setData(Constant.ID, userData.getString(Constant.ID))
                        session.setData(
                            Constant.NAME,
                            userData.getString(Constant.NAME)
                        )
                        session.setData(Constant.EMAIL, userData.getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD, userData.getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE, userData.getString(Constant.MOBILE))
                        session.setData(
                            Constant.BANK_ACCOUNT_NUMBER,
                            userData.getString(Constant.BANK_ACCOUNT_NUMBER)
                        )
                        session.setData(Constant.IFSC_CODE, userData.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, userData.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, userData.getString(Constant.BRANCH))
                        session.setData(
                            Constant.AADHAR_CARD,
                            userData.getString(Constant.AADHAR_CARD)
                        )
                        session.setData(Constant.RESUME, userData.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, userData.getString(Constant.PHOTO))
                        session.setData(
                            Constant.EDUCATION_CERTIFICATE,
                            userData.getString(Constant.EDUCATION_CERTIFICATE)
                        )
                        session.setData(Constant.JOIN_DATE, userData.getString(Constant.JOIN_DATE))
                        session.setData(
                            Constant.SALARY_DATE,
                            userData.getString(Constant.SALARY_DATE)
                        )
                        session.setData(Constant.BRANCH_ID, userData.getString(Constant.BRANCH_ID))
                        session.setData(Constant.ROLE, userData.getString(Constant.ROLE))
                        session.setData(Constant.BALANCE, userData.getString(Constant.BALANCE))
                        session.setData(Constant.STATUS, userData.getString(Constant.STATUS))
                        session.setData(
                            Constant.TOTAL_JOININGS,
                            userData.getString(Constant.SUPPORTS)
                        )
                        session.setData(Constant.TOTAL_LEADS, userData.getString(Constant.LEADS))
                        session.setData(Constant.SALARY, userData.getString(Constant.SALARY))
                        session.setData(
                            Constant.INCENTIVE_EARN,
                            userData.getString(Constant.INCENTIVES)
                        )
                        session.setData(Constant.TOTAL_EARNINGS, userData.getString(Constant.EARN))

                        session.setData(
                            Constant.STAFF_DISPLAY_ID,
                            userData.getString(Constant.STAFF_ID)
                        )
                        session.setData(Constant.DOB, userData.getString(Constant.DOB))


initCall()
                        // extract other values as needed
                    } else {
                        Toast.makeText(
                            requireContext(), jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFFS_DETAILS, params, true)

    }


}