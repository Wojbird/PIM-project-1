package com.example.pim_project_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pim_project_1.util.PrefUtilTimer

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PrefUtilTimer.setTimerState(SecondFragment.TimerState.Stopped, context)
        PrefUtilTimer.setAlarmSetTime(0, context)
    }
}