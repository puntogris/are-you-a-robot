package com.puntogris.areyouarobot.ui.settings

import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.databinding.FragmentPlayerSettingsBinding
import com.puntogris.areyouarobot.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerSettingsFragment : BaseFragment<FragmentPlayerSettingsBinding>(R.layout.fragment_player_settings) {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun initializeViews() {
        binding.playerNameSettings.setText(sharedPref.getPlayerName())

        binding.saveSettings.setOnClickListener {
            val input = binding.playerNameSettings.text.toString()
            sharedPref.setPlayerName(input)
            findNavController().navigate(R.id.welcomeFragment)
        }

    }
}
