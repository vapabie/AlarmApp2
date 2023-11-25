package com.example.alarmapp2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapp2.AlarmAdapter
import com.example.alarmapp2.AlarmApplication
import com.example.alarmapp2.AlarmViewModel
import com.example.alarmapp2.AlarmViewModelFactory
import com.example.alarmapp2.databinding.FragmentAlarmsBinding

class AlarmsFragment : Fragment() {

    private val viewModel: AlarmViewModel by activityViewModels {
        AlarmViewModelFactory(
            (activity?.application as AlarmApplication).database.alarmDao()
        )
    }

    private var _binding: FragmentAlarmsBinding? = null
    private  val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlarmAdapter { clickedAlarm ->
            val action =
                AlarmsFragmentDirections.actionAlarmsFragmentToAddAlarmFragment(id)
            findNavController().navigate(action)
        }

        binding.alarmList.layoutManager = LinearLayoutManager(this.context)
        binding.alarmList.adapter = adapter

        viewModel.allAlarms.observe(this.viewLifecycleOwner) {
                items -> items.let { adapter.submitList(it) }
        }

        binding.addButton.setOnClickListener{
            val action = AlarmsFragmentDirections.actionAlarmsFragmentToAddAlarmFragment(id)
            this.findNavController().navigate(action)
        }
    }
}