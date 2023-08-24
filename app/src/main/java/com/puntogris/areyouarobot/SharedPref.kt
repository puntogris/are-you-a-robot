package com.puntogris.areyouarobot

import android.content.Context
import androidx.preference.PreferenceManager
import com.puntogris.areyouarobot.utils.Constants.PLAYER_NAME_PREF
import com.puntogris.areyouarobot.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPlayerName(): String {
        val defaultName = Utils.createDefaultRandomName()
        return sharedPref.getString(PLAYER_NAME_PREF, defaultName) ?: defaultName
    }

    fun setPlayerName(input: String) {
        val playerName = input.ifEmpty { Utils.createDefaultRandomName() }
        sharedPref.edit().putString(PLAYER_NAME_PREF, playerName).apply()
    }

    fun showRobotAnimation() = sharedPref.contains(PLAYER_NAME_PREF)
}