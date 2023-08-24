package com.puntogris.multiplayer.utils

import android.content.res.ColorStateList
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.puntogris.multiplayer.R

@BindingAdapter("searchButtonState")
fun Button.setSearchButtonState(isSearching: Boolean) {
    if (isSearching) {
        setText(R.string.cancel_search)
        backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                com.puntogris.areyouarobot.R.color.colorPrimary,
                null
            )
        )
    } else {
        setText(R.string.search_opponent)
        backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                com.puntogris.areyouarobot.R.color.colorAccent,
                null
            )
        )
    }
}