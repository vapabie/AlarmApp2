package com.example.alarmapp2.adapter

import androidx.recyclerview.widget.DiffUtil
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp2.databinding.SoundListItemBinding
import android.view.LayoutInflater
import com.example.alarmapp2.data.Sound

class SoundAdapter : ListAdapter<Sound, SoundAdapter.SoundViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        return SoundViewHolder(
            SoundListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val currentSound = getItem(position)
        holder.bind(currentSound)
    }

    class SoundViewHolder(private var binding: SoundListItemBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(sound: Sound){
                binding.title.text = sound.soundTitle
            }
        }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Sound>() {
            override fun areItemsTheSame(oldAlarm: Sound, newAlarm: Sound): Boolean{
                return oldAlarm == newAlarm
            }

            override fun areContentsTheSame(oldAlarm: Sound, newAlarm: Sound): Boolean {
                return oldAlarm.soundUrl == newAlarm.soundUrl
            }
        }
    }
}

