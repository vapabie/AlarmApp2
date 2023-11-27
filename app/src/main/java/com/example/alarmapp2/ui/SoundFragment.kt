package com.example.alarmapp2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapp2.AlarmApplication

import com.example.alarmapp2.databinding.FragmentSoundBinding
import com.example.alarmapp2.viewmodel.SoundViewModel
import com.example.alarmapp2.viewmodel.SoundViewModelFactory
import com.example.alarmapp2.adapter.SoundAdapter


class SoundFragment : Fragment() {

    private val viewModel: SoundViewModel by activityViewModels{
        SoundViewModelFactory(
           (activity?.application as AlarmApplication).soundRepository)

    }


    private var _binding: FragmentSoundBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SoundAdapter()
        binding.soundsRv.adapter = adapter
        binding.soundsRv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allSounds.observe(viewLifecycleOwner) { items ->
            items.let { adapter.submitList(it) }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}