package com.example.alarmapp2.data

import androidx.lifecycle.LiveData
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
    suspend fun insert(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("select * from sound_table order by title asc")
    fun getSounds(): LiveData<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sound: List<Alarm>)
}
