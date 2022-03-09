package com.daniebeler.dailytasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment(), TodoAdapter.OnItemClickListener, TodoAdapter.OnItemLongclickListener {

    private var rv_dashboard: RecyclerView? = null
    private var day = ""
    lateinit var mainActivity: MainActivity
    lateinit var dbHandler: DBHandler

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)

        mainActivity = activity as MainActivity
        dbHandler = mainActivity.dbHandler
        Log.d("state", "ListFragment: initialized mainActivity")
        day = requireArguments().getString("day", "today")



        Log.d("state", "ListFragment: initialized rv_dashboard")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_dashboard = requireView().findViewById(R.id.rv_dashboard_today)
        rv_dashboard?.layoutManager = LinearLayoutManager(activity?.applicationContext)

        refreshList()
    }

    fun refreshList(){
        Log.d("state", "ListFragment: refreshing list")
        //mainActivity.dbHandler.getToDos(day)
        if(rv_dashboard == null) {
            Log.d("state", "ListFragment: rv_Dashboard is null!!!!!!!!!!")
        }

        rv_dashboard?.adapter = TodoAdapter(dbHandler.getToDos(day), this, this)
        Log.d("state", "ListFragment: refreshing done")
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