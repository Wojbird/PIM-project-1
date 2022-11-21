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
import com.example.pim_project_1.util.PrefUtilTimer

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    companion object {

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtilTimer.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }
    enum class TimerState{
        Stopped, Paused, Running, Setting
    }

    private lateinit var timerTimer: CountDownTimer
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
    ): View {

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
            setTimerManually()
            updateButtons()
        }

        binding.buttonTimerStart.setOnClickListener{
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        binding.buttonTimerPause.setOnClickListener {
            timerTimer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        binding.buttonTimerReset.setOnClickListener {
            timerTimer.cancel()
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
            timerTimer.cancel()
        }

        PrefUtilTimer.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext().applicationContext)
        PrefUtilTimer.setSecondsRemaining(secondsRemaining, requireContext().applicationContext)
        PrefUtilTimer.setTimerState(timerState, requireContext().applicationContext)
    }

    private fun initTimer(){
        timerState = PrefUtilTimer.getTimerState(requireContext().applicationContext)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtilTimer.getSecondsRemaining(requireContext().applicationContext)
        else
            timerLengthSeconds

        //resume where we left off
        val alarmSetTime = PrefUtilTimer.getAlarmSetTime(requireContext().applicationContext)
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

        PrefUtilTimer.setSecondsRemaining(timerLengthSeconds, requireContext().applicationContext)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timerTimer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setTimerManually(){
        timerState =  TimerState.Stopped

        val text = binding.editTextInput.text.toString()

        val lengthInMinutes: Int = if(text.isNotEmpty()){
            PrefUtilTimer.getTimerLengthFromEditText(text.toInt(), requireContext().applicationContext)
        } else{
            PrefUtilTimer.getTimerLength(requireContext().applicationContext)
        }

        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBar.max = timerLengthSeconds.toInt()
        PrefUtilTimer.setSecondsRemaining(timerLengthSeconds, requireContext().applicationContext)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtilTimer.getTimerLength(requireContext().applicationContext)
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtilTimer.getPreviousTimerLengthSeconds(requireContext(). applicationContext)
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
                binding.buttonTimerStart.isEnabled = false
                binding.buttonTimerPause.isEnabled = true
                binding.buttonTimerReset.isEnabled = true
            }
            TimerState.Stopped -> {
                binding.editTextInput.isVisible = false
                binding.buttonSet.isVisible = false
                binding.buttonTimerStart.isEnabled = true
                binding.buttonTimerPause.isEnabled = false
                binding.buttonTimerReset.isEnabled = true
            }
            TimerState.Paused -> {
                binding.editTextInput.isVisible = false
                binding.buttonSet.isVisible = false
                binding.buttonTimerStart.isEnabled = true
                binding.buttonTimerPause.isEnabled = false
                binding.buttonTimerReset.isEnabled = true
            }
            TimerState.Setting -> {
                binding.editTextInput.isVisible = true
                binding.buttonSet.isVisible = true
                binding.buttonTimerStart.isEnabled = true
                binding.buttonTimerPause.isEnabled = false
                binding.buttonTimerReset.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}