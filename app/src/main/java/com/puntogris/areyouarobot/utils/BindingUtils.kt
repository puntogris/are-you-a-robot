package com.puntogris.areyouarobot.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("scoreToString")
fun TextView.scoreToString(score:Int){
    text = score.toString()
}