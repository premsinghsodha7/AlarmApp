package com.prem.alarmapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.prem.alarmapp.data.entities.Alarms
import com.prem.alarmapp.databinding.AlarmItemsBinding
import com.prem.alarmapp.utils.Util

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(var viewBinding: AlarmItemsBinding) : RecyclerView.ViewHolder(viewBinding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Alarms>() {
        override fun areItemsTheSame(oldItem: Alarms, newItem: Alarms): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Alarms, newItem: Alarms): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var alarmItems: List<Alarms>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
//        val view = AlarmItemsBinding.inflate(LayoutInflater.from(parent.context) , parent,false)
//        return AlarmViewHolder(view)
        return AlarmViewHolder(
            AlarmItemsBinding.inflate(
                LayoutInflater.from(parent.context)
                , parent,
                false)
        )
//        return AlarmViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.alarm_items,
//                parent,
//                false
//            )
//        )
    }

    override fun getItemCount() = alarmItems.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentAlarm = alarmItems[position]

        holder.viewBinding.timeTv.text = Util.convertTime(currentAlarm.time)
        holder.viewBinding.daysTv.text = Util.convertDate(currentAlarm.time)
        holder.viewBinding.isActive.isChecked = currentAlarm.AlarmIsEnabled

        //this basically checks the state of the switch
        holder.viewBinding.isActive.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheckChangeListener?.let {it(isChecked, currentAlarm)}
        }

        holder.viewBinding.root.setOnClickListener {view->
            onItemClickListener?.let {it(view, currentAlarm)}
        }
    }

    private var onCheckChangeListener : ((Boolean, Alarms) -> Unit)? = null
    private var onItemClickListener : ((View?, Alarms) -> Unit)? = null

    fun setOnActiveCheckChangeListener(listener:(Boolean, Alarms) -> Unit){
        onCheckChangeListener = listener
    }

    fun setOnItemClickListener(listener:(View?, Alarms) -> Unit){
        onItemClickListener = listener
    }
}
