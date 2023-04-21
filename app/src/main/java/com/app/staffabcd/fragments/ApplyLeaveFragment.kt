package com.app.staffabcd.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.LeavesAdapter
import com.app.staffabcd.databinding.FragmentApplyLeaveBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Leaves
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ApplyLeaveFragment : Fragment() {

    lateinit var binding: FragmentApplyLeaveBinding
    lateinit var session: Session
    lateinit var  leavesAdapter: LeavesAdapter

    private val fromDateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val selectedDate = selectedCalendar.time



            binding.etDate.setText(String.format("%tF", selectedDate))


        }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentApplyLeaveBinding.inflate(inflater, container, false)
        session = Session(requireContext())

        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                fromDateSetListener,
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }


        binding.btnApply.setOnClickListener {
            val date = binding.etDate.text.toString()
            val reason = binding.edReason.text.toString()
            if (session.getData(Constant.STATUS).equals("0")) {
                Toast.makeText(requireContext(), "Account not verified", Toast.LENGTH_SHORT).show()
            } else
                applyLeave(date, reason)

        }
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvLeaves.layoutManager = linearLayoutManager
        leaveLists()
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            leaveLists()
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

    private fun applyLeave(date: String,  reason: String) {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.DATE] = date
            this[Constant.REASON] = reason

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(
                            requireContext(),
                            jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.etDate.setText("")
                        binding.edReason.setText("")
                        leaveLists()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFF_LEAVES, params, true)

    }
    private fun leaveLists() {
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
                        val leaves: ArrayList<Leaves> = ArrayList<Leaves>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Leaves =
                                    g.fromJson(jsonObject1.toString(), Leaves::class.java)
                                leaves.add(group)
                            } else {
                                break
                            }
                        }
                        leavesAdapter = LeavesAdapter(requireActivity(), leaves)
                        binding.rvLeaves.adapter = leavesAdapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.STAFF_LEAVE_LIST, params, true)
    }

}