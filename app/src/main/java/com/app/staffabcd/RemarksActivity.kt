package com.app.staffabcd

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.app.staffabcd.databinding.ActivityRemarksBinding
import com.app.staffabcd.databinding.ActivitySplashBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class RemarksActivity : AppCompatActivity() {

    lateinit var binding: ActivityRemarksBinding
    lateinit var activity: RemarksActivity
    lateinit var session: Session
    lateinit var id:String
    lateinit var remarks:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemarksBinding.inflate(layoutInflater)

        activity=this
        session= Session(activity)

        id = intent.getStringExtra("id").toString()
        remarks = intent.getStringExtra("remarks").toString()

        binding.etRemarks.setText(remarks.toString())




        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnUpdate.setOnClickListener {
            UpdateRemark()
        }


        return setContentView(binding.root)




    }











    private fun UpdateRemark() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.USER_ID] = id
            this[Constant.REMARKS] =  binding.etRemarks.text.toString()

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE).toString(),Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE).toString(),Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.UPDATE_REMARKS, params, true)

    }

}