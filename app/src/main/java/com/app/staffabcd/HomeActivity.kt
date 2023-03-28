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
import com.app.staffabcd.fragments.HomeFragment
import com.app.staffabcd.fragments.IncentiveFragment
import com.app.staffabcd.fragments.ProfileFragment
import com.app.staffabcd.fragments.WithdrawalFragment
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
        container=binding.FrameLyt
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Initialize the navigation view and set the item selected listener
        val navView: NavigationView = binding.navView
        supportFragmentManager.beginTransaction().replace(container.id, HomeFragment()).commit()
        navView.setCheckedItem(R.id.nav_home)
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(container.id, HomeFragment()).commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction().replace(container.id, ProfileFragment()).commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_incentive -> {
                    supportFragmentManager.beginTransaction().replace(container.id, IncentiveFragment()).commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_withdrawal -> {
                    supportFragmentManager.beginTransaction().replace(container.id, WithdrawalFragment()).commit()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    // Handle the logout option
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
}