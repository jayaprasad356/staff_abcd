package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
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
            requireActivity().supportFragmentManager
        )
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

}
