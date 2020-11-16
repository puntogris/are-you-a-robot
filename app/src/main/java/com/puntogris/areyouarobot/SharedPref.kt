package com.puntogris.areyouarobot

import android.content.Context
import androidx.preference.PreferenceManager
import com.puntogris.areyouarobot.utils.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPlayerName(): String{
        val playerName = sharedPref!!.getString("player_name", Utils.createDefaultRandomName())
        return playerName!!
    }

    fun setPlayerName(input: String){
        val playerName = if (input.isEmpty()) Utils.createDefaultRandomName() else input
        sharedPref.edit().putString("player_name", playerName).apply()
    }

    fun showRobotAnimation() = sharedPref.contains("player_name")
}