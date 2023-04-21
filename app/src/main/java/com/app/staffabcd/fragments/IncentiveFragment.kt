package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.IncentiveAdapter
import com.app.staffabcd.databinding.FragmentIncentiveBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Incentive
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class IncentiveFragment : Fragment() {

    lateinit var incentiveAdapter: IncentiveAdapter
    lateinit var binding: FragmentIncentiveBinding
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncentiveBinding.inflate(inflater, container, false)
        session = Session(requireActivity())
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            incentiveList()

            swipeRefreshLayout.isRefreshing = false
        }
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvReport.layoutManager = linearLayoutManager
        incentiveList()
        return binding.root
    }

    private fun incentiveList() {
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
        val incentive: ArrayList<Incentive> = ArrayList<Incentive>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Incentive =
                                    g.fromJson(jsonObject1.toString(), Incentive::class.java)
                                incentive.add(group)
                            } else {
                                break
                            }
                        }
//        incentive.add(defaultIncentive)
//        incentive.add(defaultIncentive1)

        incentiveAdapter = IncentiveAdapter(requireActivity(), incentive)
        binding.rvReport.setAdapter(incentiveAdapter)

                    }else{
                        Toast.makeText(requireContext(),"No Data Found",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
            }
           }
        }, requireActivity(), Constant.INCENTIVES_URL, params, true)
    }

}