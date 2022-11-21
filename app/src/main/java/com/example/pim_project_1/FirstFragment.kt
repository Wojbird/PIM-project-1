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
import androidx.navigation.fragment.findNavController
import com.example.pim_project_1.databinding.FragmentFirstBinding
import com.example.pim_project_1.util.PrefUtilStopwatch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtilStopwatch.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }
    enum class StopwatchState{
        Stopped, Paused, Running
    }

    private lateinit var stopwatchTimer: CountDownTimer
    private var stopwatchLengthSeconds: Long = 0
    private var stopwatchState = StopwatchState.Stopped

    private var oneDaySeconds: Long = 86400
    private var secondsRemaining: Long = 0

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToTimer.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonStopwatchStart.setOnClickListener{
            startStopwatch()
            stopwatchState = StopwatchState.Running
            updateButtons()
        }

        binding.buttonStopwatchPause.setOnClickListener {
            stopwatchTimer.cancel()
            stopwatchState = StopwatchState.Paused
            updateButtons()
        }

        binding.buttonStopwatchReset.setOnClickListener {
            stopwatchTimer.cancel()
            onStopwatchFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initStopwatch()
        removeAlarm(requireContext().applicationContext)
    }

    override fun onPause() {
        super.onPause()

        if (stopwatchState == StopwatchState.Running){
            stopwatchTimer.cancel()
        }

        PrefUtilStopwatch.setPreviousStopwatchLengthSeconds(stopwatchLengthSeconds, requireContext().applicationContext)
        PrefUtilStopwatch.setSecondsRemaining(secondsRemaining, requireContext().applicationContext)
        PrefUtilStopwatch.setStopwatchState(stopwatchState, requireContext().applicationContext)
    }

    private fun initStopwatch(){
        stopwatchState = PrefUtilStopwatch.getStopwatchState(requireContext().applicationContext)

        //we don't want to change the length of the stopwatch which is already running
        //if the length was changed in settings while it was backgrounded
        if (stopwatchState == StopwatchState.Stopped)
            setNewStopwatchLength()
        else
            setPreviousStopwatchLength()

        secondsRemaining = if (stopwatchState == StopwatchState.Running || stopwatchState == StopwatchState.Paused)
            PrefUtilStopwatch.getSecondsRemaining(requireContext().applicationContext)
        else
            stopwatchLengthSeconds

        //resume where we left off
        val alarmSetTime = PrefUtilStopwatch.getAlarmSetTime(requireContext().applicationContext)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onStopwatchFinished()
        else if (stopwatchState == StopwatchState.Running)
            startStopwatch()

        updateButtons()
        updateCountdownUI()
    }

    private fun onStopwatchFinished(){
        stopwatchState = StopwatchState.Stopped

        //set the length of the stopwatch to be the one set in SettingsActivity
        //if the length was changed when the stopwatch was running
        setNewStopwatchLength()

        binding.progressBar.progress = 0

        PrefUtilStopwatch.setSecondsRemaining(stopwatchLengthSeconds, requireContext().applicationContext)
        secondsRemaining = stopwatchLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startStopwatch(){
        stopwatchState = StopwatchState.Running

        stopwatchTimer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onStopwatchFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewStopwatchLength(){
        val lengthInMinutes = PrefUtilStopwatch.getStopwatchLength(requireContext().applicationContext)
        stopwatchLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBar.max = 60
    }

    private fun setPreviousStopwatchLength(){
        stopwatchLengthSeconds = PrefUtilStopwatch.getPreviousStopwatchLengthSeconds(requireContext(). applicationContext)
        binding.progressBar.max = 60
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = (oneDaySeconds - secondsRemaining) / 60
        val hoursUntilFinished = minutesUntilFinished / 60
        val secondsInMinuteUntilFinished = (oneDaySeconds - secondsRemaining) - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        val minutesInHourUntilFinished = minutesUntilFinished - hoursUntilFinished * 60
        val minutesStr = minutesInHourUntilFinished.toString()
        val hoursStr = hoursUntilFinished.toString()
        binding.textViewCounter.text = "${if (hoursStr.length == 2) hoursStr else "0" + hoursStr}:${if (minutesStr.length == 2) minutesStr else "0" + minutesStr}:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        binding.progressBar.progress = ((stopwatchLengthSeconds - secondsRemaining)%60).toInt()
    }

    private fun updateButtons(){
        when (stopwatchState) {
            StopwatchState.Running ->{
                binding.buttonStopwatchStart.isEnabled = false
                binding.buttonStopwatchPause.isEnabled = true
                binding.buttonStopwatchReset.isEnabled = true
            }
            StopwatchState.Stopped -> {
                binding.buttonStopwatchStart.isEnabled = true
                binding.buttonStopwatchPause.isEnabled = false
                binding.buttonStopwatchReset.isEnabled = true
            }
            StopwatchState.Paused -> {
                binding.buttonStopwatchStart.isEnabled = true
                binding.buttonStopwatchPause.isEnabled = false
                binding.buttonStopwatchReset.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}