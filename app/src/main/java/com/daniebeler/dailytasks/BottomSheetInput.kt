package com.daniebeler.dailytasks

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class BottomSheetInput : BottomSheetDialogFragment() {

    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var dbHandler: DBHandler
    var date = ""

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.setOnShowListener {
            Handler().post {
                val bottomSheet = (dialog as? BottomSheetDialog)?.findViewById<View>(R.id.bottomholder) as? ConstraintLayout
                bottomSheet?.let {
                    BottomSheetBehavior.from(it).state = STATE_EXPANDED
                    BottomSheetBehavior.from(it).skipCollapsed = true
                }
            }
        }

        return dialog
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        date = requireArguments().getString("date", "today")

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return inflater.inflate(R.layout.bottom_sheet_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        dbHandler = DBHandler(requireActivity().applicationContext)

        editText = view.findViewById(R.id.et_dialog)
        editText.requestFocus()

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        button = view.findViewById(R.id.btn_dialog)
        button.setOnClickListener {
            saveAndClose()
        }

        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
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

            dbHandler.addToDo(toDo, date)
            (activity as MainActivity).refresh(date)
            dismiss()
        }
    }
}