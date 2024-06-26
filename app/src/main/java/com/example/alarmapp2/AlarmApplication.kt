package com.example.alarmapp2

import android.app.Application
import androidx.room.Room
import com.example.alarmapp2.data.AlarmRoomDatabase
import com.example.alarmapp2.repository.SoundRepository

class AlarmApplication : Application() {

    // Using by lazy to initialize the database when it's first accessed
    val database: AlarmRoomDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AlarmRoomDatabase::class.java,
            "alarm_database"
        ).build()
    }

    val soundRepository: SoundRepository
        get() = SoundRepository(database.soundDao())




    override fun onCreate() {
        super.onCreate()
        val context = applicationContext
    }
}
