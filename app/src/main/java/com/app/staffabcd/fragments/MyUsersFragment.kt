package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.adapter.MyUsersAdapter
import com.app.staffabcd.databinding.FragmentMyUsersBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Users
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyUsersFragment : Fragment() {

    lateinit var  myUsersAdapter: MyUsersAdapter
    lateinit var binding: FragmentMyUsersBinding
    lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyUsersBinding.inflate(inflater, container, false)
        session=Session(requireActivity())

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvMyUsers.layoutManager = linearLayoutManager
        myUserLists()
        return binding.root
    }
    private fun myUserLists() {
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
                        val users: ArrayList<Users> = ArrayList<Users>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Users =
                                    g.fromJson(jsonObject1.toString(), Users::class.java)
                                users.add(group)
                            } else {
                                break
                            }
                        }
                        myUsersAdapter = MyUsersAdapter(requireActivity(), users)
                        binding.rvMyUsers.adapter = myUsersAdapter
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.MY_USERS_LIST, params, true)
    }

}