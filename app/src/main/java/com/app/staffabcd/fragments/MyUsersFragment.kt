package com.app.staffabcd.fragments

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import java.io.File
import java.io.FileOutputStream

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
        binding.btnDownload.setOnClickListener {
            createPdf()
        }
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            myUserLists()
            swipeRefreshLayout.isRefreshing = false
        }
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
                    }else{
                        Toast.makeText(requireContext(),"No Data Found",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.MY_USERS_LIST, params, true)
    }

    private fun createPdf() {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        val title = "User Information"
        paint.textSize = 40f
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(title, (pageInfo.pageWidth / 2).toFloat(), 50f, paint)

        val y = 150
        val x = 50
        val titleWidth = 150
        val contentWidth = 200

        paint.textSize = 20f
        paint.color = Color.GRAY
        canvas.drawText("Name", x.toFloat(), y.toFloat(), paint)
        canvas.drawText("Refer Code", (x + titleWidth).toFloat(), y.toFloat(), paint)
        canvas.drawText("Total Codes", (x + titleWidth + contentWidth).toFloat(), y.toFloat(), paint)
        canvas.drawText("Worked Days", (x + titleWidth + contentWidth * 2).toFloat(), y.toFloat(), paint)
        canvas.drawText("Mobile", (x + titleWidth + contentWidth * 3).toFloat(), y.toFloat(), paint)
        canvas.drawText("Total Referrals", (x + titleWidth + contentWidth * 4).toFloat(), y.toFloat(), paint)

        paint.color = Color.BLACK
        paint.textSize = 18f
        var currentY = y + 50
        myUsersAdapter.users.forEach { user ->
            user.name?.let { canvas.drawText(it, x.toFloat(), currentY.toFloat(), paint) }
            user.refer_code?.let { canvas.drawText(it, (x + titleWidth).toFloat(), currentY.toFloat(), paint) }
            user.total_codes?.let { canvas.drawText(it, (x + titleWidth + contentWidth).toFloat(), currentY.toFloat(), paint) }
            user.worked_days?.let { canvas.drawText(it, (x + titleWidth + contentWidth * 2).toFloat(), currentY.toFloat(), paint) }
            user.mobile?.let { canvas.drawText(it, (x + titleWidth + contentWidth * 3).toFloat(), currentY.toFloat(), paint) }
            user.total_referrals?.let { canvas.drawText(it, (x + titleWidth + contentWidth * 4).toFloat(), currentY.toFloat(), paint) }
            currentY += 50
        }

        document.finishPage(page)
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, "my_document.pdf")
        val outputStream = FileOutputStream(file)
        document.writeTo(outputStream)
        document.close()
        outputStream.flush()
        outputStream.close()

        Toast.makeText(activity, "PDF created successfully.", Toast.LENGTH_SHORT).show()
    }


}