package com.example.alarmapp2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("select * from alarm_table order by time asc")
    fun getAlarms(): Flow<List<Alarm>>

    @Query("select * from alarm_table where id = :id")
    fun getAlarm(id:Int): Flow<Alarm>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)


}

@Dao
interface SoundDao{
    @Query("select * from sound_table order by title asc")
    fun getSounds(): LiveData<List<Sound>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSound(sound: Sound)



}
