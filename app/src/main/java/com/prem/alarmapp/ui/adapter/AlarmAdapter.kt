package com.prem.alarmapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.prem.alarmapp.R
import com.prem.alarmapp.data.entities.Alarms

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val time_tv = itemView.findViewById<TextView>(R.id.time_tv)
        val days_tv = itemView.findViewById<TextView>(R.id.days_tv)
        val isActive = itemView.findViewById<SwitchMaterial>(R.id.isActive)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Alarms>() {
        override fun areItemsTheSame(oldItem: Alarms, newItem: Alarms): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarms, newItem: Alarms): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var alarmItems: List<Alarms>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.alarm_items,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = alarmItems.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentAlarm = alarmItems[position]

        holder.time_tv.text = currentAlarm.time
        holder.days_tv.text = currentAlarm.repeatDays

        //this basically checks the state of the switch
        holder.isActive.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                currentAlarm.AlarmIsEnabled = true
//
//                CreateAlarmActivity.selectedDays.clear()
//
//                CreateAlarmActivity.startAlarm(currentAlarm.id, holder.itemView.context)
//                alarmViewModel.update(currentAlarm)
//            } else {
//                currentAlarm.AlarmIsEnabled = false
//                CreateAlarmActivity.cancelAlarm(currentAlarm.id,holder.itemView.context)
//                alarmViewModel.update(currentAlarm)
//            }

            onCheckChangeListener?.let {it(isChecked, currentAlarm)}
        }

        holder.isActive.isChecked = currentAlarm.AlarmIsEnabled
    }

    private var onCheckChangeListener : ((Boolean, Alarms) -> Unit)? = null

    fun setOnActiveCheckChangeListener(listener:(Boolean, Alarms) -> Unit){
        onCheckChangeListener = listener
    }
}
