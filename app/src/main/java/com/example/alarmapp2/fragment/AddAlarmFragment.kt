package com.example.alarmapp2.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alarmapp2.AlarmAdapter
import com.example.alarmapp2.AlarmApplication
import com.example.alarmapp2.AlarmViewModel
import com.example.alarmapp2.AlarmViewModelFactory
import com.example.alarmapp2.data.Alarm
import com.example.alarmapp2.databinding.FragmentAddAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime

class AddAlarmFragment : Fragment() {

    private var _binding: FragmentAddAlarmBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AlarmsFragmentArgs by navArgs()


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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun bind(alarm: Alarm){
        binding.apply{
            addAlarmMessage.setText(alarm.alarmMessage, TextView.BufferType.SPANNABLE)
            addAlarmSound.editText?.setText(alarm.alarmSound, TextView.BufferType.SPANNABLE)
            addAlarmTime.text = alarmFormattedTime(alarm.alarmTime)
            saveAlarmButton.setOnClickListener {
                updateAlarm(alarm.id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNewAlarm() {
        if (isEntryValid()) {
            val alarmTime = parseFormattedTime(binding.addAlarmTime.text.toString())
            viewModel.addNewAlarm(
                alarmTime,
                binding.addAlarmMessage.text.toString(),
                binding.addAlarmSound.editText?.text.toString()
            )
            navigateBackToAlarmsFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAlarm(alarmId: Int) {
        if (isEntryValid()) {
            val alarmTime = parseFormattedTime(binding.addAlarmTime.text.toString())
            viewModel.updateAlarm(
                navigationArgs.alarmId,
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun alarmFormattedTime(time: LocalDateTime): String {
        return String.format("%02d:%02d", time.hour, time.minute)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseFormattedTime(formattedTime: String): LocalDateTime {
        val parts = formattedTime.split(":")
        return LocalDateTime.now()
            .withHour(parts[0].toInt())
            .withMinute(parts[1].toInt())
            .withSecond(0)
    }

    val adapter = AlarmAdapter { clickedAlarm ->
        val action = AlarmsFragmentDirections.actionAlarmsFragmentToAddAlarmFragment(id)
        findNavController().navigate(action)
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

        val id = navigationArgs.alarmId
        if (id > 0){
            viewModel.retrieveAlarm(id).observe(this.viewLifecycleOwner){selectedAlarm ->
                val alarm = selectedAlarm
                bind(alarm)
            }
        } else {
            binding.saveAlarmButton.setOnClickListener{
                addNewAlarm()
            }
        }
    }








}