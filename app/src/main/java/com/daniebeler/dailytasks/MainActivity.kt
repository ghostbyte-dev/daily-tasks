package com.daniebeler.dailytasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.daniebeler.dailytasks.ui.composables.MyMainScreen
import com.daniebeler.dailytasks.ui.theme.DailyTasksTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHandler = DBHandler(this)


        setContent {
            DailyTasksTheme {
                MyMainScreen()
            }
        }
    }
}