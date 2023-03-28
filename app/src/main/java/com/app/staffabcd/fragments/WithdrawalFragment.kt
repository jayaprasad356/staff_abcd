package com.app.staffabcd.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast


import com.app.staffabcd.R
import com.app.staffabcd.Utils
import com.app.staffabcd.databinding.FragmentWithdrawalBinding


class WithdrawalFragment : Fragment() {

lateinit var binding: FragmentWithdrawalBinding
lateinit var btnWithdaw: Button
lateinit var activity:Activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        binding = FragmentWithdrawalBinding.inflate(inflater, container, false)
        btnWithdaw=binding.btnWithdraw
        activity= requireActivity()
        btnWithdaw.setOnClickListener{
            val bankDetailFragment = BankDetailFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.FrameLyt, bankDetailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        return binding.root
    }

}