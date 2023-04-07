package com.app.staffabcd

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.staffabcd.databinding.ActivityHomeBinding
import com.app.staffabcd.fragments.*
import com.app.staffabcd.helper.Constant
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var container: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        // Initialize the drawer layout and toggle button
        drawerLayout = binding.drawerLayout
        container = binding.FrameLyt
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Initialize the navigation view and set the item selected listener
        val navView: NavigationView = binding.navView
        supportFragmentManager.beginTransaction().replace(container.id, HomeFragment()).commit()
        navView.setCheckedItem(R.id.nav_home)
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        if (!Constant.DEBUG)
            showFillDocumentPopup(toolbar)

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
}