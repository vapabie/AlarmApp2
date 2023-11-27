package com.example.alarmapp2.repository

import androidx.lifecycle.LiveData
import com.example.alarmapp2.data.Alarm
import com.example.alarmapp2.data.Sound
import com.example.alarmapp2.data.SoundDao


class SoundRepository(private val soundDao: SoundDao) {

    fun getAllSounds(): LiveData<List<Sound>> {
        return soundDao.getSounds()
    }






}