package com.app.staffabcd.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.TransactionAdapter
import com.app.staffabcd.databinding.FragmentTransactionBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Transanction
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TransactionFragment : Fragment() {
lateinit var  transactionAdapter: TransactionAdapter
lateinit var binding:FragmentTransactionBinding
lateinit var session:Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        session=Session(requireActivity())

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvTransaction.layoutManager = linearLayoutManager
        transactionList()
        return binding.root
    }

    private fun transactionList() {
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
                        val transanctions: ArrayList<Transanction> = ArrayList<Transanction>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Transanction =
                                    g.fromJson(jsonObject1.toString(), Transanction::class.java)
                                transanctions.add(group)
                            } else {
                                break
                            }
                        }
                        transactionAdapter = TransactionAdapter(requireActivity(), transanctions)
                        binding.rvTransaction.setAdapter(transactionAdapter)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFFS_TRANSACTION_LIST, params, true)
    }

}