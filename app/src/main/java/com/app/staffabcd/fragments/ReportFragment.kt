package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.staffabcd.adapter.ViewPagerAdapter
import com.app.staffabcd.databinding.FragmentReportBinding


class ReportFragment : Fragment() {

    lateinit var binding: FragmentReportBinding
    var viewPagerAdapter: ViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        viewPagerAdapter = ViewPagerAdapter(
            childFragmentManager
        )
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            binding.viewPager.adapter = viewPagerAdapter

            binding.tabLayout.setupWithViewPager(binding.viewPager)
            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

}
