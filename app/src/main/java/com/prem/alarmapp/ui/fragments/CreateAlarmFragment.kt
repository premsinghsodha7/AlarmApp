package com.prem.alarmapp.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prem.alarmapp.R
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.receiver.AlarmReceiver
import com.prem.alarmapp.ui.AlarmViewModel
import com.prem.alarmapp.utils.Status
import java.util.*

class CreateAlarmFragment(
    var viewModel: AlarmViewModel? = null
) : Fragment(R.layout.fragment_create_alarm),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    val args: CreateAlarmFragmentArgs by navArgs()
    lateinit var AM_PM: String

    private lateinit var cg_days_chips: ChipGroup
    private lateinit var timeTV: TextView
    private lateinit var btn_choose_time: MaterialButton
    private lateinit var fab_save: ExtendedFloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)

        subscribeToObservers()

        cg_days_chips = view.findViewById(R.id.cg_days_chips)
        timeTV = view.findViewById(R.id.timeTV)
        btn_choose_time = view.findViewById(R.id.btn_choose_time)
        fab_save = view.findViewById(R.id.fab_save)

        //set title
        requireActivity().title = "Schedule Alarm"

        addDayChips()

        btn_choose_time.setOnClickListener(this)
        fab_save.setOnClickListener(this)
    }

    private fun subscribeToObservers() {
        viewModel!!.insertAlarmItemStatus.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {result ->
                when(result.status){
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView().rootView,
                            "Successfully saved alarm",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView().rootView,
                            result.message?: "An unknown error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        /* No-OP */
                    }
                }
            }
        })

        viewModel!!.updateAlarmItemStatus.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {result ->
                when(result.status){
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView().rootView,
                            "Updated Successfully",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView().rootView,
                            result.message?: "An unknown error occurred",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        /* No-OP */
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.btn_choose_time){
            val calendar= Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, min ->
                selectedHour = hour
                selectedMin = min

                var hourString = selectedHour.toString()
                var minString = selectedMin.toString()
                if ( selectedHour > 12) {
                    hourString = ( selectedHour - 12).toString()
                    AM_PM = "PM"
                } else {
                    AM_PM = "AM"
                }

                if (selectedMin < 10) {
                    minString = "0${selectedMin}"
                }
                val formattedTime = "$hourString:$minString $AM_PM"

                timeTV.text=formattedTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }else if (v!!.id == R.id.fab_save){
            if(TextUtils.isEmpty(timeTV.text)){
                Snackbar.make(
                    requireView().rootView,
                    "please select a time",
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            }
            saveAlarm()
        }
    }

    private fun saveAlarm() {
        val timeText = timeTV.text.toString()
        val builder = StringBuilder()
        val alarmIsOn = true
        var repeatdays: String? = null

        when (selectedDays.size) {
            0 -> {
                repeatdays = "Alarm"
            }
            1 -> selectedDays.forEachIndexed { _, day ->
                repeatdays = "Alarm, $day"
            }
            in 2..6 -> {
                selectedDays.forEachIndexed { _, days ->
                    val formatted = days.substring(6, 9)
                    builder.append("$formatted ")
                    repeatdays = builder.toString()
                }
            }
            else -> repeatdays = "Every day"
        }

        viewModel!!.insertAlarmItem(timeText, repeatdays?:"", alarmIsOn)
    }

    private fun addDayChips() {
        days.forEach { day ->
            cg_days_chips.addChip {
                text = day
                tag = day
                isCheckable
                isClickable
                setOnCheckedChangeListener(this@CreateAlarmFragment)
            }
        }
    }

    private fun ChipGroup.addChip(chipInitializer: Chip.() -> Unit) {
        val dayChip =
            layoutInflater.inflate(R.layout.layout_day_chip, null).findViewById<Chip>(R.id.chip_day)
        dayChip.setChipBackgroundColorResource(R.color.Cardbackground_color1)

        val chip = dayChip.apply {
            chipInitializer(this)
        }
        addView(chip)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            selectedDays.add(buttonView?.tag.toString())
        } else {
            selectedDays.apply {
                removeAt(indexOf(buttonView?.tag.toString()))
            }
        }
    }

    companion object {
        var selectedDays = mutableListOf<String>()
        private var selectedHour = 0
        private var selectedMin = 0
        const val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
        const val RUN_WEEKLY = (7 * 24 * 60 * 60 * 1000).toLong()
        fun startAlarm(alarmId:Int,context: Context) {
            selectedDays.forEachIndexed { _, day ->
                /**
                 * //Sunday's value is 1 and so index + 1
                 * @see Calendar.SUNDAY
                 */
                val indexOfDay = days.indexOf(day) + 1

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, indexOfDay)
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMin)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
//                if (calendar.before(Calendar.getInstance())) {
//                    calendar.add(Calendar.DATE, 7)
//                }
                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar[Calendar.DAY_OF_WEEK] = calendar[Calendar.DAY_OF_WEEK] + 1
                }

                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, alarmId, intent, 0)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    RUN_WEEKLY,
                    pendingIntent
                )
            }

            if (selectedDays.isEmpty()) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMin)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                // if alarm time has already passed, increment day by 1
                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH] + 1
                }

                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, alarmId, intent, 0)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }

        }

        //cancel alarm function
        fun cancelAlarm(id: Int, context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, id, intent, 0)
            alarmManager.cancel(pendingIntent)
        }

        const val ALARM_TIME = "ALARM_TIME"
        const val ALARM_IsON = "ALARM_IsON"
        const val ALARM_REPEAT_DAYS = "ALARM_REPEAT_DAYS"

        //list of repeat days
        private val days by lazy {
            listOf(
                "Every Sunday",
                "Every Monday",
                "Every Tuesday",
                "Every Wednesday",
                "Every Thursday",
                "Every Friday",
                "Every Saturday"
            )
        }
    }
}