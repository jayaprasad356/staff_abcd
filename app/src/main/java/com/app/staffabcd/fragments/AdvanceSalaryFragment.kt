package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.staffabcd.databinding.FragmentAdvanceSalaryBinding

class AdvanceSalaryFragment : Fragment() {

lateinit var binding: FragmentAdvanceSalaryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdvanceSalaryBinding.inflate(inflater, container, false)


        return binding.root

    }

}