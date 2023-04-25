package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.MyPerformanceAdapter
import com.app.staffabcd.databinding.FragmentMyPerformanceBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Performance
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MyPerformanceFragment : Fragment() {
    lateinit var  myPerformanceAdapter: MyPerformanceAdapter
    lateinit var binding: FragmentMyPerformanceBinding
    lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPerformanceBinding.inflate(inflater, container, false)
        session= Session(requireActivity())
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            myPerformance()
            swipeRefreshLayout.isRefreshing = false
        }
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvMyUsers.layoutManager = linearLayoutManager
        myPerformance()
        return binding.root
    }
    private fun myPerformance() {

//        val performances: ArrayList<Performance> = ArrayList<Performance>()
//        val report1 = Performance("11-12-2022", "250", "6", "5",)
//        val report2 = Performance("12-12-2022", "250", "7", "5")
//        performances.add(report1)
//        performances.add(report2)
//        myPerformanceAdapter = MyPerformanceAdapter(requireActivity(), performances)
//        binding.rvMyUsers.adapter = myPerformanceAdapter
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
                        val performances: ArrayList<Performance> = ArrayList<Performance>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Performance =
                                    g.fromJson(jsonObject1.toString(), Performance::class.java)
                                performances.add(group)
                            } else {
                                break
                            }
                        }
                        myPerformanceAdapter = MyPerformanceAdapter(requireActivity(), performances)
                        binding.rvMyUsers.adapter = myPerformanceAdapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.MYPERFORMANCE, params, true)
    }
}