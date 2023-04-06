package com.app.staffabcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.app.staffabcd.databinding.ActivityHomeBinding
import com.app.staffabcd.databinding.ActivitySplashBinding
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val SPLASH_TIME_OUT = 3000L // 5 seconds
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        session = Session(this)
        Handler().postDelayed({
            // Start HomeActivity after 5 seconds
            if (session.getBoolean(Constant.IS_LOGIN)) {
                val i = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(i)
                finish()
            }else{
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }

            // Close this activity
            finish()
        }, SPLASH_TIME_OUT)
        return setContentView(binding.root)
    }
}