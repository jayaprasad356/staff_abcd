package com.app.staffabcd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        goToActivity()
   // showUpdateDialog("link")
        return setContentView(binding.root)
    }
    private fun showUpdateDialog(link: String) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(this)

        // Set the title and message
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Please update to continue using the app.")

        // Set the dialog to be cancelable false
        builder.setCancelable(false)

        // Add the positive button and its click listener
        builder.setPositiveButton("Update") { dialog, which ->
            // Redirect the user to the Google Play Store or your server's download page
            // to download the latest version of the app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }

        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }
    private fun goToActivity(){
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
    }

}