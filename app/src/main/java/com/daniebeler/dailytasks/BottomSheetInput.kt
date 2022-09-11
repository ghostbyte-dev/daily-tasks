package com.daniebeler.dailytasks

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BottomSheetInput : BottomSheetDialogFragment() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private var date = ""
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        date = requireArguments().getString("date", "today")

        return inflater.inflate(R.layout.bottom_sheet_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.et_dialog)
        editText.requestFocus()

        button = view.findViewById(R.id.btn_dialog)
        button.setOnClickListener {
            saveAndClose()
        }

        editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                saveAndClose()

                return@OnKeyListener true
            }
            false
        })
    }

    private fun saveAndClose(){

        if (editText.text.isNotEmpty()) {
            val toDo = ToDoItem()
            toDo.name = editText.text.toString()

            if(date == "today"){
                toDo.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
            else{
                toDo.date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }

            mainActivity.dbHandler.addToDo(toDo)
            mainActivity.refresh(date)
            dismiss()
        }
    }
}