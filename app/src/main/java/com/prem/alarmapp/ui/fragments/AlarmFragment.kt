package com.prem.alarmapp.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.prem.alarmapp.R
import com.prem.alarmapp.ui.adapter.AlarmAdapter
import com.prem.alarmapp.ui.fragments.AlarmFragment.Toast.displaySuccessToast
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

    private var adapter: AlarmAdapter = AlarmAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        viewModel = viewModel?:ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)

        recyclerView = view.findViewById(R.id.recycler_view)
        emptyRecView = view.findViewById(R.id.emptyRecView)

        subscribeToObservers()
        setupRecyclerview()
    }

    private fun subscribeToObservers(){
        viewModel!!.getAllAlarms().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                emptyRecView.visibility = View.VISIBLE
            } else {
                emptyRecView.visibility = View.GONE
            }
            adapter.alarmItems = it
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
                    //get item adapter position
                    val deletedAlarm = adapter.alarmItems[adapterPosition]
                    //delete it from the view model
                    viewModel!!.delete(deletedAlarm)
                    //cancel its alarm
                    cancelAlarm(deletedAlarm.id!!, requireContext())
                    //set its alarm to false
                    deletedAlarm.AlarmIsEnabled = false
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
        }

    private fun setupRecyclerview(){
        recyclerView.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu_items, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    companion object {

        //cancel alarm function
        fun cancelAlarm(id: Int, context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }
    }
}





