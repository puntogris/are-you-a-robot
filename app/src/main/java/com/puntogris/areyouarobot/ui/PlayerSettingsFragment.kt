package com.puntogris.areyouarobot.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.databinding.FragmentPlayerSettingsBinding
import com.puntogris.areyouarobot.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerSettingsFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlayerSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_settings, container, false)

        binding.playerNameSettings.setText(sharedPref.getPlayerName())

        binding.saveSettings.setOnClickListener {
            val input = binding.playerNameSettings.text.toString()
            sharedPref.setPlayerName(input)
            findNavController().navigate(R.id.welcomeFragment)
        }

        return binding.root
    }

}
