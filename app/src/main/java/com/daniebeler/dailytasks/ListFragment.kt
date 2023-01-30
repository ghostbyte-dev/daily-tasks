package com.daniebeler.dailytasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment(), TodoAdapter.OnItemClickListener, TodoAdapter.OnItemLongclickListener {

    private var recyclerView: RecyclerView? = null
    private lateinit var textView: TextView
    private var day = ""
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)

        mainActivity = activity as MainActivity
        day = requireArguments().getString("day", "today")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = requireView().findViewById(R.id.rv_dashboard_today)
        recyclerView?.layoutManager = LinearLayoutManager(activity?.applicationContext)

        textView = requireView().findViewById(R.id.tv_empty)

        textView.visibility = View.GONE

        Log.d("state", "Listfragment: onViewCreated id:" + this)

        refreshList()
    }

    fun refreshList(){
        recyclerView?.adapter = TodoAdapter(mainActivity.dbHandler.getToDos(day), this, this)

        Log.d("loog", (recyclerView?.adapter)?.itemCount.toString())

        if ((recyclerView?.adapter)?.itemCount == 0) {
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(position: Int) {
        mainActivity.dbHandler.updateToDo(position, day)
        refreshList()
    }

    override fun onItemLongClick(position: Int) {
        mainActivity.dbHandler.deleteToDo(position, day)
        refreshList()
    }
}