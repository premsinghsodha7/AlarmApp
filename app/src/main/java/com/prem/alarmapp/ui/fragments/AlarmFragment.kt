package com.prem.alarmapp.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prem.alarmapp.R
import com.prem.alarmapp.ui.adapter.AlarmAdapter
import com.prem.alarmapp.ui.AlarmViewModel
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.prem.alarmapp.receiver.AlarmReceiver
import com.prem.alarmapp.utils.Status
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

class AlarmFragment(
    var viewModel: AlarmViewModel? = null
) : Fragment(R.layout.fragment_alarm) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyRecView: RelativeLayout
    private lateinit var fabAdd: FloatingActionButton

    private lateinit var alarmAdapter: AlarmAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel?:ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycler_view)
        emptyRecView = view.findViewById(R.id.emptyRecView)
        fabAdd = view.findViewById(R.id.fabAdd)

        subscribeToObservers()
        setupRecyclerview()

        fabAdd.setOnClickListener{
            it.findNavController().navigate(R.id.action_alarmFragment_to_create_new_alarm)
        }
    }

    private fun subscribeToObservers(){
        viewModel!!.getAllAlarms().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                emptyRecView.visibility = View.VISIBLE
            } else {
                emptyRecView.visibility = View.GONE
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
                    //delete it from the view model
                    viewModel!!.delete(deletedAlarm)
                    //cancel its alarm
                    CreateAlarmFragment.cancelAlarm(deletedAlarm.id!!, requireContext())
                    //set its alarm to false
                    deletedAlarm.AlarmIsEnabled = false
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

    private fun setupRecyclerview(){
        alarmAdapter = AlarmAdapter()
        recyclerView.apply {
            adapter = alarmAdapter
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }

        alarmAdapter.setOnActiveCheckChangeListener { b, alarms ->
            alarms.AlarmIsEnabled = b
            if (b){
                CreateAlarmFragment.selectedDays.clear()
                CreateAlarmFragment.startAlarm(alarms.id!!, requireContext())
            }else{
                CreateAlarmFragment.cancelAlarm(alarms.id!!, requireContext())
            }
            viewModel!!.updateAlarmItem(alarms.id, alarms.time, alarms.repeatDays, alarms.AlarmIsEnabled)
        }

        alarmAdapter.setOnItemClickListener { view, alarms ->

        }
    }

}