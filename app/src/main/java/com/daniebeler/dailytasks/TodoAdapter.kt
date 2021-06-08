package com.daniebeler.dailytasks

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(val list: MutableList<ToDoItem>, val listener: OnItemClickListener, val longlistener: OnItemLongclickListener) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.toDoName.text = list[position].name

        if(list[position].isCompleted){
            holder.toDoName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.toDoName.setTextColor(ContextCompat.getColor(context, R.color.grey))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener{
        val toDoName : TextView = v.findViewById(R.id.tv_todo_name)

        init {
            v.setOnClickListener(this)

            v.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                longlistener.onItemLongClick(position)
            }

            return true
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    interface OnItemLongclickListener{
        fun onItemLongClick(position: Int)
    }

}