package com.example.alarmapp2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.alarmapp2.data.Alarm
import com.example.alarmapp2.data.AlarmDao
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AlarmViewModel(private val alarmDao: AlarmDao) : ViewModel() {

    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAlarms().asLiveData()


    fun updateAlarm(
        alarmId: Int,
        alarmTime: LocalDateTime,
        alarmMessage: String,
        alarmSound: String
    ){
        val updatedAlarm = getUpdatedAlarmEntry(
            alarmId,
            alarmTime,
            alarmMessage,
            alarmSound
        )

        updateAlarm(updatedAlarm)
    }


    fun updateAlarm(alarm: Alarm){
        viewModelScope.launch { alarmDao.updateAlarm(alarm) }
    }
    private fun getUpdatedAlarmEntry(
        alarmId: Int,
        alarmTime: LocalDateTime,
        alarmMessage: String,
        alarmSound:String
    ): Alarm {
        return Alarm(
            id = alarmId,
            alarmTime = alarmTime,
            alarmMessage = alarmMessage,
            alarmSound = alarmSound
        )
    }

    fun addNewAlarm(
        alarmTime: LocalDateTime,
        alarmMessage: String,
        alarmSound: String
    ){
        val newAlarm = getNewAlarmEntry(alarmTime, alarmMessage, alarmSound)
        insertAlarm(newAlarm)
    }

    private fun getNewAlarmEntry(
        alarmTime: LocalDateTime,
        alarmMessage: String,
        alarmSound: String
    ): Alarm {
        return Alarm(
            alarmTime = alarmTime,
            alarmMessage = alarmMessage,
            alarmSound = alarmSound
        )
    }

    private fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch { alarmDao.insertAlarm(alarm) }
    }

    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch { alarmDao.deleteAlarm(alarm) }
    }

    fun isEntryValid( alarmMessage: String, alarmSound: String
    ): Boolean {
        if(alarmSound.isBlank() || alarmMessage.isBlank()){
            return false
        }
        return true
    }

    fun retrieveAlarm(id:Int): LiveData<Alarm>{
        return alarmDao.getAlarm(id).asLiveData()
    }



}



class AlarmViewModelFactory(private val alarmDao: AlarmDao): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
