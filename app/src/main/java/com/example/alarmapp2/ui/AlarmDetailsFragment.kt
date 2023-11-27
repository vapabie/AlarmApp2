package com.example.alarmapp2.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alarmapp2.AlarmApplication
import com.example.alarmapp2.R
import com.example.alarmapp2.data.Alarm
import com.example.alarmapp2.databinding.FragmentAlarmDetailsBinding
import com.example.alarmapp2.viewmodel.AlarmViewModel
import com.example.alarmapp2.viewmodel.AlarmViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDateTime


class AlarmDetailsFragment : Fragment() {
    private val navigationArgs: AlarmDetailsFragmentArgs by navArgs()
    lateinit var alarm: Alarm

    private val viewModel: AlarmViewModel by activityViewModels {
        AlarmViewModelFactory(
            (activity?.application as AlarmApplication).database.alarmDao()
        )
    }

    private var _binding: FragmentAlarmDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmDetailsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    private fun bind(alarm: Alarm){
        binding.apply {
            alarmMessage.setText(alarm.alarmMessage)
            alarmTime.text = alarmFormattedTime(alarm.alarmTime)
            alarmSound.text = alarm.alarmSound
            deleteButton.setOnClickListener{showConfirmationDialog()}
            editButton.setOnClickListener { editItem() }

        }
    }

    private fun editItem() {
        val action = AlarmDetailsFragmentDirections.actionAlarmDetailsFragmentToAddAlarmFragment(
            alarm.id)
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteAlarm()
            }
            .show()
    }

    private fun deleteAlarm() {
        viewModel.deleteAlarm(alarm)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.alarmId

        viewModel.retrieveAlarm(id).observe(this.viewLifecycleOwner){ selectedItem  ->
            alarm = selectedItem
            bind(alarm)
        }

    }

    private fun alarmFormattedTime(time: LocalDateTime): String {
        return String.format("%02d:%02d", time.hour, time.minute)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}