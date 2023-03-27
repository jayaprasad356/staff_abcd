package com.app.staffabcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.app.staffabcd.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    var binding: ActivityRegistrationBinding? = null
   lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        btnRegister=binding!!.btnRegister
        btnRegister.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }



        return setContentView(binding!!.root)
    }
}