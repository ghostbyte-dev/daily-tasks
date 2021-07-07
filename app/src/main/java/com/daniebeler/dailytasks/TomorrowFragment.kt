package com.daniebeler.dailytasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TomorrowFragment : Fragment(), TodoAdapter.OnItemClickListener, TodoAdapter.OnItemLongclickListener {

    private var dbHandler: DBHandler? = null
    private var rv_dashboard: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_tomorrow, container, false)

        dbHandler = DBHandler(requireActivity().applicationContext)

        rv_dashboard = view.findViewById(R.id.rv_dashboard_tomorrow)
        rv_dashboard?.layoutManager = LinearLayoutManager(activity?.applicationContext)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshList()
    }

    fun refreshList(){
        rv_dashboard!!.adapter = TodoAdapter(dbHandler!!.getToDos("tomorrow"), this, this)
    }

    override fun onItemClick(position: Int) {
        dbHandler!!.updateToDo(position, "tomorrow")
        refreshList()
    }

    override fun onItemLongClick(position: Int) {
        dbHandler!!.deleteToDo(position, "tomorrow")
        refreshList()
    }
}