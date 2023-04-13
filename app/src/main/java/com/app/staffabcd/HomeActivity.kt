package com.app.staffabcd

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.staffabcd.databinding.ActivityHomeBinding
import com.app.staffabcd.databinding.NavHeaderBinding
import com.app.staffabcd.fragments.*
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var container: FrameLayout
    lateinit var session: Session
    lateinit var navView: NavigationView
    lateinit var navHeaderBinding: NavHeaderBinding
    val toolbar: androidx.appcompat.widget.Toolbar by lazy { binding.toolbar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        navHeaderBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))

        session = Session(this)

        staffDetails()

        // Initialize the drawer layout and toggle button
        drawerLayout = binding.drawerLayout
        container = binding.FrameLyt
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navHeaderBinding.navHeaderEmail.text = session.getData(Constant.EMAIL)
        navHeaderBinding.navHeaderName.text = session.getData(Constant.FIRST_NAME)

        // Initialize the navigation view and set the item selected listener
        navView = binding.navView


        toolbar.setTitle(R.string.home)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(container.id, HomeFragment())
                        .commit()
                    toolbar.setTitle(R.string.home)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, ProfileFragment()).commit()
                    toolbar.setTitle(R.string.profile)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_incentive -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, IncentiveFragment()).commit()
                    toolbar.setTitle(R.string.incentive)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_withdrawal -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, WithdrawalFragment()).commit()
                    toolbar.setTitle(R.string.withdrawal)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_documents -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, DocumentFragment()).commit()
                    toolbar.setTitle(R.string.documents)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_transaction -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, TransactionFragment()).commit()
                    toolbar.setTitle(R.string.transaction)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_report -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, ReportFragment()).commit()
                    toolbar.setTitle(R.string.report)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_applyLeave -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, ApplyLeaveFragment()).commit()
                    toolbar.setTitle(R.string.apply_leave)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_adv_salary -> {
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, AdvanceSalaryFragment()).commit()
                    toolbar.setTitle(R.string.adv_salary)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }

        }
        return setContentView(binding.root)
    }

    private fun staffDetails() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(
                            this, jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        val message = jsonObject.getString("message")
                        val documentUpload = jsonObject.getInt("document_upload")
                        val salary = jsonObject.getString("salary")
                        val incentiveEarn = jsonObject.getString("incentive_earn")
                        val totalEarnings = jsonObject.getInt("total_earnings")
                        val totalLeads = jsonObject.getString("total_leads")
                        val totalJoinings = jsonObject.getString("total_joinings")

                        session.setData(Constant.DOCUMENT_UPLOAD, documentUpload.toString())
                        session.setData(Constant.SALARY, salary)
                        session.setData(Constant.INCENTIVE_EARN, incentiveEarn)
                        session.setData(Constant.TOTAL_EARNINGS, totalEarnings.toString())
                        session.setData(Constant.TOTAL_LEADS, totalLeads)
                        session.setData(Constant.TOTAL_JOININGS, totalJoinings)

                        val userData: JSONObject =
                            jsonObject.getJSONArray(Constant.DATA).getJSONObject(0)
                        session.setData(Constant.ID, userData.getString(Constant.ID))
                        session.setData(
                            Constant.FIRST_NAME,
                            userData.getString(Constant.FIRST_NAME)
                        )
                        session.setData(Constant.LAST_NAME, userData.getString(Constant.LAST_NAME))
                        session.setData(Constant.EMAIL, userData.getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD, userData.getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE, userData.getString(Constant.MOBILE))
                        session.setData(
                            Constant.BANK_ACCOUNT_NUMBER,
                            userData.getString(Constant.BANK_ACCOUNT_NUMBER)
                        )
                        session.setData(Constant.IFSC_CODE, userData.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, userData.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, userData.getString(Constant.BRANCH))
                        session.setData(
                            Constant.AADHAR_CARD,
                            userData.getString(Constant.AADHAR_CARD)
                        )
                        session.setData(Constant.RESUME, userData.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, userData.getString(Constant.PHOTO))
                        session.setData(
                            Constant.EDUCATION_CERTIFICATE,
                            userData.getString(Constant.EDUCATION_CERTIFICATE)
                        )
                        session.setData(Constant.JOIN_DATE, userData.getString(Constant.JOIN_DATE))
                        session.setData(
                            Constant.SALARY_DATE,
                            userData.getString(Constant.SALARY_DATE)
                        )
                        session.setData(Constant.BRANCH_ID, userData.getString(Constant.BRANCH_ID))
                        session.setData(Constant.ROLE, userData.getString(Constant.ROLE))
                        session.setData(Constant.BALANCE, userData.getString(Constant.BALANCE))
                        session.setData(Constant.STATUS, userData.getString(Constant.STATUS))
                        session.setData(
                            Constant.STAFF_DISPLAY_ID,
                            userData.getString(Constant.STAFF_ID)
                        )
                        session.setData(Constant.DOB,userData.getString(Constant.DOB))
                        if (!Constant.DEBUG) {
                            if (!(session.getData(Constant.DOCUMENT_UPLOAD).toString().equals("1")))
                                showFillDocumentPopup(toolbar)
                        }

                        supportFragmentManager.beginTransaction()
                            .replace(container.id, HomeFragment()).commit()
                        navView.setCheckedItem(R.id.nav_home)
                        // extract other values as needed
                    } else {
                        Toast.makeText(
                            this, jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, this, Constant.STAFFS_DETAILS, params, true)

    }

    private fun showFillDocumentPopup(toolbar: androidx.appcompat.widget.Toolbar) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please Upload Document")
        builder.setMessage("Do you want to upload a document now?")
        builder.setPositiveButton("OK") { dialog, which ->
            supportFragmentManager.beginTransaction()
                .replace(container.id, DocumentFragment()).commit()
            toolbar.setTitle(R.string.documents)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        builder.setNegativeButton("Cancel", null)
        val alertDialog = builder.create()
        alertDialog.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the toggle button click
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        // Clear all the shared preferences data
        val sharedPreferences = getSharedPreferences("staffabcd", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        finishAffinity()
    }
    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val currentFragment = supportFragmentManager.findFragmentById(container.id)
            if (currentFragment is HomeFragment) {
                super.onBackPressed()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(container.id, HomeFragment()).commit()
                toolbar.setTitle(R.string.home)
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

        }

    }
}