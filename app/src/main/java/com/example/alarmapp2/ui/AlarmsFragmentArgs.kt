package com.example.alarmapp2.ui

import android.os.Bundle
import androidx.navigation.NavArgs

data class AlarmsFragmentArgs(val alarmId: Int) : NavArgs {
    fun toBundle(): Bundle {
        val result = Bundle()
        result.putInt("alarmId", this.alarmId)
        return result
    }

    companion object {
        @JvmStatic
        fun fromBundle(bundle: Bundle): AlarmsFragmentArgs {
            bundle.classLoader = AlarmsFragmentArgs::class.java.classLoader
            val alarmId = bundle.getInt("alarmId")
            return AlarmsFragmentArgs(alarmId)
        }
    }
}