package com.app.staffabcd.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.app.staffabcd.R
import com.app.staffabcd.databinding.FragmentAdvanceSalaryBinding
import com.app.staffabcd.databinding.FragmentApplyLeaveBinding
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApplyLeaveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApplyLeaveFragment : Fragment() {

    lateinit var binding: FragmentApplyLeaveBinding

    private val fromDateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val selectedDate = selectedCalendar.time

            val toDateString = binding.etToDate.text.toString()
            if (toDateString.isNotEmpty()) {
                val toCalendar = Calendar.getInstance()
                val toYear = toCalendar.get(Calendar.YEAR)
                val toMonth = toCalendar.get(Calendar.MONTH)
                val toDayOfMonth = toCalendar.get(Calendar.DAY_OF_MONTH)
                toCalendar.set(toYear, toMonth, toDayOfMonth)
                val toDate = toCalendar.time

                if (toDate.compareTo(selectedDate) < 0) {
                    binding.etFromDate.setText("")
                    binding.etToDate.setText("")
                    Toast.makeText(requireContext(), "From date must be before or equal to To date", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }
            }

            binding.etFromDate.setText(String.format("%tF", selectedDate))
        }

    private val toDateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            val selectedDate = selectedCalendar.time

            val fromDateString = binding.etFromDate.text.toString()
            if (fromDateString.isNotEmpty()) {
                val fromCalendar = Calendar.getInstance()
                val fromYear = fromCalendar.get(Calendar.YEAR)
                val fromMonth = fromCalendar.get(Calendar.MONTH)
                val fromDayOfMonth = fromCalendar.get(Calendar.DAY_OF_MONTH)
                fromCalendar.set(fromYear, fromMonth, fromDayOfMonth)
                val fromDate = fromCalendar.time

                if (selectedDate.compareTo(fromDate) < 0) {
                    binding.etFromDate.setText("")
                    binding.etToDate.setText("")
                    Toast.makeText(requireContext(), "To date must be after or equal to From date", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }
            }

            binding.etToDate.setText(String.format("%tF", selectedDate))
        }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentApplyLeaveBinding.inflate(inflater, container, false)

        binding.etFromDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                fromDateSetListener,
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }

        binding.etToDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                toDateSetListener,
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }

        binding.btnApply.setOnClickListener {
            val fromDate = binding.etFromDate.text.toString()
            val toDate = binding.etToDate.text.toString()
            val reason = binding.edReason.text.toString()
            // Do something with the selected dates and reason, e.g. submit a leave application

        }

        return binding.root
    }
}