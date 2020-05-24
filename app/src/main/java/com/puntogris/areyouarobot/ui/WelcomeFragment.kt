package com.puntogris.areyouarobot.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentWelcomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_welcome,container,false)

        binding.welcomeFragment = this

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.playerSettingsFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun navigateToGame(){
        findNavController().navigate(R.id.action_welcomeFragment_to_singlePlayerGameFragment)
    }

}
