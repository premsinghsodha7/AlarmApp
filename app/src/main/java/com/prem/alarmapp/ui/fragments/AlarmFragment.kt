package com.prem.alarmapp.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prem.alarmapp.R
import com.prem.alarmapp.databinding.FragmentAlarmBinding
import com.prem.alarmapp.ui.adapter.AlarmAdapter
import com.prem.alarmapp.ui.viewmodels.AlarmViewModel
import com.prem.alarmapp.service.AlarmService
import com.prem.alarmapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmFragment(
    var viewModel: AlarmViewModel? = null
) : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var alarmAdapter: AlarmAdapter

    @Inject
    lateinit var alarmService: AlarmService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //alarmService = AlarmService(requireContext())
        //init viewmodel
        viewModel = viewModel?:ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)

        //observe livedata from viewmodel
        subscribeToObservers()
        //setup recyclerview ie: adapters and adapter listeners
        setupRecyclerview()

        //fab add listener to schedule and save alarm in database
        binding.fabAdd.setOnClickListener{
            setAlarm {
                alarmService.setRepetitiveAlarm(it)
                viewModel!!.insertAlarmItem(it, true)
            }
        }
    }


    /*
    * to set alarm
    * callback to get selected datetime and schedule alarm accordingly
    * */
    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    //livedata observers viewmodel
    private fun subscribeToObservers(){
        viewModel!!.getAllAlarms().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.emptyRecView.visibility = View.VISIBLE
            } else {
                binding.emptyRecView.visibility = View.GONE
            }
            alarmAdapter.alarmItems = it
        })

        viewModel!!.deleteAlarmItemStatus.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {result ->
                when(result.status){
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView().rootView,
                            result.data?:"Deleted Successfully",
                            Snackbar.LENGTH_LONG
                        ).show()
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

    // swipe recyclerview item action delete item from database and cancel the scheduled alarm
    private val itemTouchCallback = //swipe delete function
        object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                if (direction == ItemTouchHelper.LEFT) {
                    val adapterPosition = viewHolder.adapterPosition
                    //get item alarmAdapter position
                    val deletedAlarm = alarmAdapter.alarmItems[adapterPosition]
                    //cancel its alarm
                    alarmService.cancelAlarm(deletedAlarm.time)
                    //delete it from the view model
                    viewModel!!.delete(deletedAlarm)
                }
            }

            //swipe delete function continues
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            Objects.requireNonNull<FragmentActivity>(
                                activity
                            ), R.color.red
                        )
                    )
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeLeftLabel("delete")
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

    //set recyclerview and adapter
    private fun setupRecyclerview(){
        binding.recyclerView.apply {
            adapter = alarmAdapter
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }

        alarmAdapter.setOnActiveCheckChangeListener { b, alarms ->
            Log.d("TAG", "setupRecyclerview: ")
            alarms.AlarmIsEnabled = b
            if (b){
                alarmService.setRepetitiveAlarm(alarms.time)
            }else{
                alarmService.cancelAlarm(alarms.time)
            }
            viewModel!!.updateAlarmItem(alarms.id!!, alarms.time, alarms.AlarmIsEnabled)
        }

        /*alarmAdapter.setOnItemClickListener { view, alarms ->}*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}