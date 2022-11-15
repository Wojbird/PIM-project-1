package com.example.pim_project_1

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.pim_project_1.databinding.FragmentSecondBinding
import com.example.pim_project_1.util.PrefUtil

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

    val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000
}
    enum class TimerState{
        Stopped, Paused, Running, Setting
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToStopwatch.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.textViewCounter.setOnClickListener {
            timerState =  TimerState.Setting
            updateButtons()

        }

        binding.buttonSet.setOnClickListener {
            timerState =  TimerState.Stopped
            updateButtons()
        }

        binding.buttonStart.setOnClickListener{v ->
            startTimer()
            timerState =  TimerState.Running
            updateButtons()
        }

        binding.buttonPause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        binding.buttonReset.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()
        removeAlarm(requireContext().applicationContext)
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(requireContext().applicationContext, nowSeconds, secondsRemaining)
            //TODO: show notification
        }
        else if (timerState == TimerState.Paused){
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext().applicationContext)
        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext().applicationContext)
        PrefUtil.setTimerState(timerState, requireContext().applicationContext)
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(requireContext().applicationContext)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(requireContext().applicationContext)
        else
            timerLengthSeconds

        //resume where we left off
        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext().applicationContext)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerLength()

        binding.progressBar.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext().applicationContext)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(requireContext().applicationContext)
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext(). applicationContext)
        binding.progressBar.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val hoursUntilFinished = minutesUntilFinished / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        val minutesInHourUntilFinished = minutesUntilFinished - hoursUntilFinished * 60
        val minutesStr = minutesInHourUntilFinished.toString()
        val hoursStr = hoursUntilFinished.toString()
        binding.textViewCounter.text = "${if (hoursStr.length == 2) hoursStr else "0" + hoursStr}:${if (minutesStr.length == 2) minutesStr else "0" + minutesStr}:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        binding.progressBar.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons(){
        when (timerState) {
            TimerState.Running ->{
                binding.editTextInput.isVisible = false
                binding.buttonSet.isVisible = false
                binding.buttonStart.isEnabled = false
                binding.buttonPause.isEnabled = true
                binding.buttonReset.isEnabled = true
            }
            TimerState.Stopped -> {
                binding.editTextInput.isVisible = false
                binding.buttonSet.isVisible = false
                binding.buttonStart.isEnabled = true
                binding.buttonPause.isEnabled = false
                binding.buttonReset.isEnabled = true
            }
            TimerState.Paused -> {
                binding.editTextInput.isVisible = false
                binding.buttonSet.isVisible = false
                binding.buttonStart.isEnabled = true
                binding.buttonPause.isEnabled = false
                binding.buttonReset.isEnabled = true
            }
            TimerState.Setting -> {
                binding.editTextInput.isVisible = true
                binding.buttonSet.isVisible = true
                binding.buttonStart.isEnabled = true
                binding.buttonPause.isEnabled = false
                binding.buttonReset.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}