package com.puntogris.multiplayer.utils

import android.content.res.ColorStateList
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import com.puntogris.areyouarobot.R

fun Button.setSearchButtonState(isSearching: Boolean) {
    if (isSearching) {
        setText(R.string.cancel_search)
        backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                R.color.colorPrimary,
                null
            )
        )
    } else {
        setText(R.string.search_opponent)
        backgroundTintList = ColorStateList.valueOf(
            ResourcesCompat.getColor(
                resources,
                R.color.colorAccent,
                null
            )
        )
    }
}
