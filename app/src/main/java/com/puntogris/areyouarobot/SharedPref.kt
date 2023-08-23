package com.puntogris.areyouarobot

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager
import com.puntogris.areyouarobot.utils.Constants.PLAYER_NAME_PREF
import com.puntogris.areyouarobot.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPlayerName() =
        sharedPref.getString(PLAYER_NAME_PREF, Utils.createDefaultRandomName()) ?: Utils.createDefaultRandomName()

    @SuppressLint("CommitPrefEdits")
    fun setPlayerName(input: String){
        val playerName = if (input.isEmpty()) Utils.createDefaultRandomName() else input
        sharedPref.edit().putString(PLAYER_NAME_PREF, playerName).apply()
    }

    fun showRobotAnimation() = sharedPref.contains(PLAYER_NAME_PREF)
}