package com.puntogris.areyouarobot.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

        binding.privacyPolicy.setOnClickListener {
            launchWebBrowserIntent("https://are-you-a-robot-web.vercel.app/privacy-policy.html")
        }

        binding.termsAndConditions.setOnClickListener {
            launchWebBrowserIntent("https://are-you-a-robot-web.vercel.app/terms-and-conditions.html")
        }
    }

    fun Fragment.launchWebBrowserIntent(uri: String) {
        try {
            Intent(Intent.ACTION_VIEW).let {
                it.data = Uri.parse(uri)
                startActivity(it)
            }
        } catch (e: Exception) {
        }
    }
}
