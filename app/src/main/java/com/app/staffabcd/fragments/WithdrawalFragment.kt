package com.app.staffabcd.fragments


import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.R
import com.app.staffabcd.adapter.WithdrawalAdapter
import com.app.staffabcd.databinding.FragmentWithdrawalBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
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
    var type: String = "incentives"


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
            val amount = binding.etAmount.text.toString().toIntOrNull()

            if(session.getData(Constant.STATUS).equals("0")){
                Toast.makeText(requireContext(),"Account Not Verified",Toast.LENGTH_SHORT).show()
            }else{
                if(type.equals("incentives")){


                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("You are eligible to withdraw only "+session.getData(Constant.INCENTIVE_PERCENTAGE)+"% of your incentive earnings. Please review with your manager for changes.\n" +
                            "                         Or\n" +
                            "Continue to withdraw your eligible incentives.")
                    builder.setPositiveButton("Continue") { dialog, which ->
                        withdrawal()
                    }
                    builder.setNegativeButton("No") { dialog, which ->
                        // Action to be performed when the user clicks the No button
                    }
                    val dialog = builder.create()
                    dialog.show()

                }else{
                    withdrawal()
                }



            }

        }
        binding.tvIncentiveBalance.text=session.getData(Constant.BALANCE).toString()
        binding.tvSalBalance.text=session.getData(Constant.SALARY_BALANCE).toString()

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvWithdrawalHistory.layoutManager = linearLayoutManager

        binding.cbIncentive.setOnClickListener {
            IncentiveWallet()

        }
        binding.cbSalary.setOnClickListener {
            salaryWallet()


        }

        withdrawalList()

        binding.btnBankDetails.setOnClickListener {
            val bankDetailFragment = BankDetailFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.FrameLyt, bankDetailFragment)
            transaction.commit()
        }
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            withdrawalList()
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }
    private fun salaryWallet() {
        binding.cbSalary.setChecked(true)
        binding.cbIncentive.setChecked(false)
        binding.rlSalaryWallet.setBackgroundResource(R.drawable.card_bg_selected)
        binding.rlIncentiveWallet.setBackgroundResource(R.drawable.card_bg)
        type = "salary"


    }

    private fun IncentiveWallet() {
        binding.cbIncentive.setChecked(true)
        binding.cbSalary.setChecked(false)
        binding.rlIncentiveWallet.setBackgroundResource(R.drawable.card_bg_selected)
        binding.rlSalaryWallet.setBackgroundResource(R.drawable.card_bg)
        type = "incentives"

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
    private fun withdrawal() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.AMOUNT] = binding.etAmount.text.toString()
            this[Constant.TYPE] = type
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()
                        val userData: JSONObject =
                            jsonObject.getJSONArray(Constant.DATA).getJSONObject(0)
                        session.setData(Constant.BALANCE, userData.getString(Constant.BALANCE))
                        session.setData(Constant.SALARY_BALANCE, userData.getString(Constant.SALARY_BALANCE))
                        binding.tvIncentiveBalance.text=session.getData(Constant.BALANCE).toString()
                        binding.tvSalBalance.text=session.getData(Constant.SALARY_BALANCE).toString()


                        withdrawalList()

                    }else {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.WITHDRAWALS, params, true)
    }



}