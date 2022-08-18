package com.daniebeler.dailytasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class StateAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragmentList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList.get(position);
    }

    fun addFragment(position: Int) {

        val todayFragment = ListFragment()
        val args = Bundle()

        if(position == 0){
            args.putString("day", "today")
        }
        else{
            args.putString("day", "tomorrow")
        }

        todayFragment.arguments = args
        fragmentList.add(todayFragment)
    }
}