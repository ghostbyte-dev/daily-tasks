package com.daniebeler.dailytasks

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdd: Button
    private lateinit var btnIvy: Button
    private lateinit var btnMode: Button
    private lateinit var bottomSheetDialog: BottomSheetDialogFragment

    private lateinit var viewPager: ViewPager2

    private lateinit var todayFragment: ListFragment
    private lateinit var tomorrowFragment: ListFragment

    lateinit var dbHandler: DBHandler

    lateinit var themeSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeSharedPreferences =  getSharedPreferences("DAILY_TASKS", Context.MODE_PRIVATE)
        if (themeSharedPreferences.getString("theme", "light") != "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        initViewPager2()

        dbHandler = DBHandler(this)

        btnAdd = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener{
            bottomSheetDialog = BottomSheetInput()
            val bundle = Bundle()
            if(viewPager.currentItem == 0){
                bundle.putString("date", "today")
            }
            else{
                bundle.putString("date", "tomorrow")
            }

            bottomSheetDialog.arguments = bundle
            bottomSheetDialog.show(supportFragmentManager, "tag")
        }

        btnIvy = findViewById(R.id.btn_ivy)
        btnIvy.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://jamesclear.com/ivy-lee")))
        }

        btnMode = findViewById(R.id.btn_mode)
        btnMode.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                themeSharedPreferences.edit().putString("theme", "dark").apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                themeSharedPreferences.edit().putString("theme", "light").apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initViewPager2(){
        viewPager = findViewById(R.id.viewpager)
        val adapter = StateAdapter(this)
        viewPager.adapter = adapter

        adapter.addFragment(0)
        adapter.addFragment(1)
        todayFragment = adapter.createFragment(0) as ListFragment
        tomorrowFragment = adapter.createFragment(1) as ListFragment

        val tabLayout:TabLayout = findViewById(R.id.tablayout)
        val names:ArrayList<String> = arrayListOf("Today", "Tomorrow")
        TabLayoutMediator(tabLayout, viewPager){tab,position ->
            tab.text = names[position]
        }.attach()
    }

    fun refresh(date:String){
        Log.d("state", "Main: refreshing")
        if(date == "today"){
            todayFragment.refreshList()
        }
        else{
            tomorrowFragment.refreshList()
        }
    }
}