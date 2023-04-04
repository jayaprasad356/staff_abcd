package com.app.staffabcd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.staffabcd.databinding.ActivityHomeBinding
import com.app.staffabcd.fragments.*
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
                        .replace(container.id, DocumentsFragment()).commit()
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
        finishAffinity()
    }
}