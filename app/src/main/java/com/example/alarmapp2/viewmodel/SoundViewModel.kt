package com.example.alarmapp2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alarmapp2.data.Sound
import com.example.alarmapp2.repository.SoundRepository

class SoundViewModel(private val soundRepository: SoundRepository) : ViewModel() {

    val allSounds: LiveData<List<Sound>> = soundRepository.getAllSounds()

    fun retrieveSound(id:Int): LiveData<List<Sound>> {
        return soundRepository.getAllSounds()
    }




}

class SoundViewModelFactory(private val soundDao: SoundRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SoundViewModel(soundDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}