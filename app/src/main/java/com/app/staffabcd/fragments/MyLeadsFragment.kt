package com.app.staffabcd.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.staffabcd.R
import com.app.staffabcd.adapter.MyLeadsAdapter
import com.app.staffabcd.adapter.MyUsersAdapter
import com.app.staffabcd.databinding.FragmentMyLeadsBinding
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


class MyLeadsFragment : Fragment() {


    lateinit var myUsersAdapter: MyLeadsAdapter
    lateinit var binding: FragmentMyLeadsBinding
    lateinit var session: Session
    val mobileNumbers: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLeadsBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        binding.shareMobileNumber.setOnClickListener {
            shareMobileNumbersInWhatsApp(mobileNumbers)
        }

        val linearLayoutManager = LinearLayoutManager(activity)
        binding.rvMyUsers.layoutManager = linearLayoutManager
        myUserLists()
        binding.btnDownload.setOnClickListener {
            generatePDF()
        }
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            myUserLists()
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root

    }


    private fun myUserLists() {
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
                        val users: ArrayList<Users> = ArrayList<Users>()
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Users = g.fromJson(jsonObject1.toString(), Users::class.java)
                                users.add(group)
                            } else {
                                break
                            }
                        }

                        // Create an ArrayList to store mobile numbers

                        // Extract and add mobile numbers to the ArrayList
                        for (user in users) {
                            val mobileNumber = user.mobile // Assuming the field name is "mobile"
                            if (mobileNumber != null) {
                                mobileNumbers.add(mobileNumber)
                            }
                        }

                        // Use the mobileNumbers ArrayList as needed
                        myUsersAdapter = MyLeadsAdapter(requireActivity(), users)
                        binding.rvMyUsers.adapter = myUsersAdapter

                        // Now the mobileNumbers ArrayList contains all the mobile numbers
                        // You can use it as needed, for example, print each mobile number
                        for (mobileNumber in mobileNumbers) {
                            Log.d("MobileNumberlist", mobileNumber)
                        }

                    } else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.MY_LEADS_LIST, params, true)
    }

    private fun generatePDF() {
        // Create a new document with A4 size
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        //   val typeface = Typeface.createFromAsset(context?.assets, "font/boldwall.ttf")
        val typeface = context?.let { context ->
            ResourcesCompat.getFont(context, R.font.gafigste)
        }

        // Initialize the paint object
        val paint = Paint()
        paint.textSize = 10f

        // Calculate the title and content widths
        val titleWidth = 120
        val contentWidth = ((page.canvas.width - titleWidth - 40) / 5)

        // Draw the table headers
        val x = 20
        var currentY = 50
        paint.typeface = typeface
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.50f
        canvas.drawRect(x.toFloat(), currentY.toFloat(), (x + titleWidth + contentWidth * 5).toFloat(), (currentY + 50).toFloat(), paint)
        paint.color = Color.BLACK
        canvas.drawText("Name", (x + 10).toFloat(), (currentY + 30).toFloat(), paint)
        canvas.drawText("Refer Code", (x + titleWidth + 10).toFloat(), (currentY + 30).toFloat(), paint)
        canvas.drawText("Total Codes", (x + titleWidth + contentWidth + 10).toFloat(), (currentY + 30).toFloat(), paint)
        canvas.drawText("Worked Days", (x + titleWidth + contentWidth * 2 + 10).toFloat(), (currentY + 30).toFloat(), paint)
        canvas.drawText("Mobile", (x + titleWidth + contentWidth * 3 + 10).toFloat(), (currentY + 30).toFloat(), paint)
        canvas.drawText("Total Referrals", (x + titleWidth + contentWidth * 4 + 10).toFloat(), (currentY + 30).toFloat(), paint)

        // Draw the table data
        currentY += 50
        myUsersAdapter.users.forEach() { user ->
            paint.color = Color.BLACK
//             Draw the cell borders
            canvas.drawRect(x.toFloat(), currentY.toFloat(), (x + titleWidth).toFloat(), (currentY + 50).toFloat(), paint)
            canvas.drawRect((x + titleWidth).toFloat(), currentY.toFloat(), (x + titleWidth + contentWidth).toFloat(), (currentY + 50).toFloat(), paint)
            canvas.drawRect((x + titleWidth + contentWidth).toFloat(), currentY.toFloat(), (x + titleWidth + contentWidth * 2).toFloat(), (currentY + 50).toFloat(), paint)
            canvas.drawRect((x + titleWidth + contentWidth * 2).toFloat(), currentY.toFloat(), (x + titleWidth + contentWidth * 3).toFloat(), (currentY + 50).toFloat(), paint)
            canvas.drawRect((x + titleWidth + contentWidth * 3).toFloat(), currentY.toFloat(), (x + titleWidth + contentWidth * 4).toFloat(), (currentY + 50).toFloat(), paint)
            canvas.drawRect((x + titleWidth + contentWidth * 4).toFloat(), currentY.toFloat(), (x + titleWidth + contentWidth * 5).toFloat(), (currentY + 50).toFloat(), paint)
            paint.color = Color.BLACK
            user.name?.let { canvas.drawText(it, (x + 10).toFloat(), (currentY + 30).toFloat(), paint) }
            user.refer_code?.let { canvas.drawText(it, (x + titleWidth + 10).toFloat(), (currentY + 30).toFloat(), paint) }
            user.total_codes?.let { canvas.drawText(it, (x + titleWidth + contentWidth + 10).toFloat(), (currentY + 30).toFloat(), paint) }
            user.worked_days?.let { workedDays ->
                val textBounds = Rect()
                paint.getTextBounds(workedDays, 0, workedDays.length, textBounds)
                val textWidth = textBounds.width()
                val centerX = x + titleWidth + contentWidth * 2 + (contentWidth - textWidth) / 2f
                canvas.drawText(workedDays, centerX, currentY + 30f, paint)
            }
            user.mobile?.let { canvas.drawText(it, (x + titleWidth + contentWidth * 3 + 10).toFloat(), (currentY + 30).toFloat(), paint) }
            user.l_referral_count?.let { canvas.drawText(it, (x + titleWidth + contentWidth * 4 + 10).toFloat(), (currentY + 30).toFloat(), paint) }
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


    private fun shareMobileNumbersInWhatsApp(mobileNumbers: ArrayList<String>) {
        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.type = "text/plain"
        val mobileNumbersText = mobileNumbers.joinToString("\n") // Join the mobile numbers with line breaks
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, mobileNumbersText)

        // Create a chooser intent to let the user select WhatsApp or WhatsApp Business
        val chooserIntent = Intent.createChooser(whatsappIntent, "Share via")

        try {
            startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            // No app available to handle the intent
            Toast.makeText(requireContext(), "No app available for sharing", Toast.LENGTH_SHORT).show()
        }
    }


}