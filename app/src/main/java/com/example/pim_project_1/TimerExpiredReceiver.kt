package com.example.pim_project_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pim_project_1.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //TODO: show notification

        PrefUtil.setTimerState(SecondFragment.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}