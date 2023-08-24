package com.puntogris.areyouarobot.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.databinding.FragmentWelcomeBinding
import com.puntogris.areyouarobot.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    @Inject
    lateinit var sharedPref: SharedPref

    override fun initializeViews() {
        binding.fragment = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.playerSettingsFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun navigateToGame() {
        findNavController().navigate(R.id.singlePlayerGameFragment)
    }
}
