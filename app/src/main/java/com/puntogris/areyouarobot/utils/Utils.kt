package com.puntogris.areyouarobot.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {

    fun createDefaultRandomName():String{
        var name = "robot"
        repeat(4) {
            name += (0..9).random().toString()
        }
        return name
    }

    fun showSoftKeyboard(view: View, activity:Activity) {
        if (view.requestFocus()) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}