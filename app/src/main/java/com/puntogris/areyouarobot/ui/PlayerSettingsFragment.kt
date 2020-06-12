package com.puntogris.areyouarobot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentPlayerSettingsBinding
import com.puntogris.areyouarobot.utils.Utils

class PlayerSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlayerSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_settings, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultPlayerName = Utils.createDefaultRandomName()
        val playerName = sharedPref!!.getString("player_name",defaultPlayerName)
        binding.playerNameSettings.setText(playerName)

        binding.saveSettings.setOnClickListener {
            var input = binding.playerNameSettings.text.toString()
            if (input.isEmpty()) input = defaultPlayerName

            with (sharedPref.edit()) {
                putString("player_name",input)
                apply()}
            findNavController().navigate(R.id.welcomeFragment)
        }

        return binding.root
    }

}
