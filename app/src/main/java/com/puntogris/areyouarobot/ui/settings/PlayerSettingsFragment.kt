package com.puntogris.areyouarobot.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.databinding.FragmentPlayerSettingsBinding
import com.puntogris.areyouarobot.databinding.FragmentRankingsBinding
import com.puntogris.areyouarobot.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.core.net.toUri

@AndroidEntryPoint
class PlayerSettingsFragment : Fragment(R.layout.fragment_player_settings) {

    @Inject
    lateinit var sharedPref: SharedPref

    private val binding by viewBinding(FragmentPlayerSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.playerNameSettings.setText(sharedPref.getPlayerName())

        binding.saveSettings.setOnClickListener {
            val input = binding.playerNameSettings.text.toString()
            sharedPref.setPlayerName(input)
            findNavController().navigate(R.id.welcomeFragment)
        }

        binding.privacyPolicy.setOnClickListener {
            launchWebBrowserIntent("https://robot.puntogris.com/privacy-policy.html")
        }

        binding.termsAndConditions.setOnClickListener {
            launchWebBrowserIntent("https://robot.puntogris.com/terms-and-conditions.html")
        }
    }

    fun Fragment.launchWebBrowserIntent(uri: String) {
        try {
            Intent(Intent.ACTION_VIEW).let {
                it.data = uri.toUri()
                startActivity(it)
            }
        } catch (e: Exception) {
        }
    }
}
