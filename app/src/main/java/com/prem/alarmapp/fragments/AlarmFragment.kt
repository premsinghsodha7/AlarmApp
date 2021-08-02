package com.prem.alarmapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prem.alarmapp.R
import com.prem.alarmapp.adapter.AlarmAdapter
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.fragments.AlarmFragment.Toast.displayFailureToast
import com.prem.alarmapp.fragments.AlarmFragment.Toast.displaySuccessToast
import com.prem.alarmapp.ui.AlarmViewModel
import com.prem.alarmapp.ui.AlarmViewModelFactory
import com.prem.alarmapp.ui.CreateAlarmActivity
import com.muddzdev.styleabletoastlibrary.StyleableToast
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_alarm.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

@Suppress("DEPRECATION")
class AlarmFragment : Fragment(R.layout.fragment_alarm), KodeinAware {
    override val kodein by kodein()
    private val factory: AlarmViewModelFactory by instance()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var viewModel: AlarmViewModel? = null
    private var adapter: AlarmAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this, factory).get(AlarmViewModel::class.java)

        adapter = AlarmAdapter(viewModel!!, listOf())

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel!!.getAllAlarms().observe(viewLifecycleOwner, Observer {
            //this checks if the recyclerview is empty
            if (it.isEmpty()) {
                emptyRecView.visibility = View.VISIBLE
            } else {
                emptyRecView.visibility = View.GONE
            }
            adapter!!.alarmList = it
            adapter!!.notifyDataSetChanged()
        })


        //swipe delete function
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
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
                    //get item adapter position
                    val deletedAlarm = adapter!!.getAlarmAt(adapterPosition)
                    //delete it from the view model
                    viewModel!!.delete(deletedAlarm)
                    //cancel its alarm
                    CreateAlarmActivity.cancelAlarm(deletedAlarm.id, requireContext())
                    //set its alarm to false
                    deletedAlarm.AlarmIsEnabled = false
                    //notifies the recyclerview that an item was removed
                    adapter!!.notifyItemRemoved(adapterPosition)

                    displaySuccessToast(requireContext(), "Alarm deleted")
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
        }).attachToRecyclerView(recyclerView)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_CANCELED && data != null) {
            // get intent data from create alarm activity
            if (requestCode == CREATE_ALARM_REQUEST && resultCode == Activity.RESULT_OK) {
                val time = data.getStringExtra(CreateAlarmActivity.ALARM_TIME)
                val repeatDay = data.getStringExtra(CreateAlarmActivity.ALARM_REPEAT_DAYS)
                val alarmIsActive = data.getBooleanExtra(CreateAlarmActivity.ALARM_IsON, true)

                val alarm = Alarms(time!!, repeatDay!!, alarmIsActive)
                //insert the alarm into database using our viewmodel instance
                Log.d("TAG", "onActivityResult: ${alarm.AlarmIsEnabled}, ${alarm.id}, ${alarm.repeatDays}, ${alarm.time}")
                viewModel!!.insert(alarm)

                displaySuccessToast(requireContext(), "alarm created successfully")
            } else {
                displayFailureToast(requireContext(), "an error occurred")
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.create_new_alarm) {
            val intent = Intent(context, CreateAlarmActivity::class.java)
            startActivityForResult(intent, CREATE_ALARM_REQUEST)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.Alarms)

        super.onStart()
    }

    // toast object
    object Toast {
        fun displaySuccessToast(context: Context, message: String) {
            StyleableToast.makeText(context, message, R.style.myToast).show()
        }

        fun displayFailureToast(context: Context, message: String) {
            StyleableToast.makeText(context, message, R.style.myToast1).show()
        }
    }

    companion object {
        const val CREATE_ALARM_REQUEST = 1
    }
}





