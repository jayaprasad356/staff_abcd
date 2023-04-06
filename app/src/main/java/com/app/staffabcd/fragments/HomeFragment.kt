package com.app.staffabcd.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.app.staffabcd.HomeActivity
import com.app.staffabcd.R
import com.app.staffabcd.databinding.FragmentDocumentsBinding
import com.app.staffabcd.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.cvJoining.setOnClickListener{

         navigateToReport()
        }
        binding.cvIncentive.setOnClickListener{
            navigateToReport()
        }
        binding.cvLead.setOnClickListener{
            navigateToReport()
        }
        binding.cvSalary.setOnClickListener{
            showMoveSalaryDialog()
        }


        return binding.root
    }

    private  fun navigateToReport(){
        val reportFragment = ReportFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.FrameLyt, reportFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun showMoveSalaryDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        val dialogView = inflater.inflate(R.layout.dialog_move_salary, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            // Perform action to move salary to wallet
            dialog.dismiss()
        }
    }


}