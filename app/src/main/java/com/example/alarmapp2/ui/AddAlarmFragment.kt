package com.example.alarmapp2.ui

import android.content.Context

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alarmapp2.AlarmApplication
import com.example.alarmapp2.R
import com.example.alarmapp2.viewmodel.AlarmViewModel
import com.example.alarmapp2.viewmodel.AlarmViewModelFactory
import com.example.alarmapp2.data.Alarm
import com.example.alarmapp2.databinding.FragmentAddAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime

class AddAlarmFragment : Fragment() {

    private var _binding: FragmentAddAlarmBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AlarmDetailsFragmentArgs by navArgs()

    lateinit var  alarm: Alarm


    private val viewModel: AlarmViewModel by activityViewModels{
        AlarmViewModelFactory(
            (activity?.application as AlarmApplication).database.alarmDao()
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAlarmBinding.inflate(inflater, container,false)
        return binding.root
    }

    private fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(
            binding.addAlarmMessage.text.toString(),
            binding.addAlarmSound.editText?.text.toString()
        )
        Log.d("is entry valid ", isEntryValid().toString())
    }


    private  fun bind(alarm: Alarm){
        val time = alarmFormattedTime(alarm.alarmTime)
        binding.apply{
            addAlarmMessage.setText(alarm.alarmMessage, TextView.BufferType.SPANNABLE)
            addAlarmSound.editText?.setText(alarm.alarmSound, TextView.BufferType.SPANNABLE)
            addAlarmTime.setText(time, TextView.BufferType.SPANNABLE)
            //addAlarmTime.text = alarmFormattedTime(alarm.alarmTime)
            saveAlarmButton.setOnClickListener {
                updateAlarm()
            }
        }
    }


    private fun addNewAlarm() {
        if (isEntryValid()) {
            val alarmTime = parseFormattedTime(binding.addAlarmTime.text.toString())
            viewModel.addNewAlarm(
                alarmTime,
                binding.addAlarmMessage.text.toString(),
                binding.addAlarmSound.editText?.text.toString()
            )
            Log.d("AddAlarmFragment", "addNewAlarm: Alarm added successfully")
            navigateBackToAlarmsFragment()
        }
    }


    private fun updateAlarm() {
        if (isEntryValid()) {
            val alarmTime = parseFormattedTime(binding.addAlarmTime.text.toString())
            viewModel.updateAlarm(
                this.navigationArgs.alarmId,
                alarmTime,
                this.binding.addAlarmMessage.text.toString(),
                this.binding.addAlarmSound.editText?.text.toString()
            )
            navigateBackToAlarmsFragment()
        }
    }

    private fun navigateBackToAlarmsFragment() {
        val action = AddAlarmFragmentDirections.actionAddAlarmFragmentToAlarmsFragment()
        findNavController().navigate(action)
    }

    // Utility functions for formatting and parsing time

    private fun alarmFormattedTime(time: LocalDateTime): String {
        return String.format("%02d:%02d", time.hour, time.minute)
    }

    private fun parseFormattedTime(formattedTime: String): LocalDateTime {
        val parts = formattedTime.split(":")
        return LocalDateTime.now()
            .withHour(parts[0].toInt())
            .withMinute(parts[1].toInt())
            .withSecond(0)
    }



    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .build()

        picker.addOnPositiveButtonClickListener {
            val selectedHour = picker.hour
            val selectedMinute = picker.minute

            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.addAlarmTime.text = formattedTime
        }

        picker.show(parentFragmentManager, "timePicker")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setTimeButton.setOnClickListener { showTimePicker() }

        val soundItems = arrayOf("Sound 1", "Sound 2", "Sound 3", "Sound 4")

        val autoCompleteTextView: AutoCompleteTextView = view.findViewById(R.id.autoCompleteTextView)

        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_menu, soundItems)

        autoCompleteTextView.setAdapter(adapter)

        val alarmId = navigationArgs.alarmId // Retrieve the passed alarm ID

        if (alarmId > 0) {
            viewModel.retrieveAlarm(alarmId).observe(this.viewLifecycleOwner) {
                    selectedAlarm -> bind(selectedAlarm) // Populate the fields with the existing alarm data
            }
        }else {
            binding.saveAlarmButton.setOnClickListener {
                addNewAlarm()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }


}