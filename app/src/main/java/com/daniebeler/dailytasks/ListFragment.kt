package com.daniebeler.dailytasks

import android.os.Bundle
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_list, container, false)

        mainActivity = activity as MainActivity
        day = requireArguments().getString("day", "today")

        rv_dashboard = view.findViewById(R.id.rv_dashboard_today)
        rv_dashboard?.layoutManager = LinearLayoutManager(activity?.applicationContext)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshList()
    }

    fun refreshList(){
        rv_dashboard?.adapter = TodoAdapter(mainActivity.dbHandler.getToDos(day), this, this)
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