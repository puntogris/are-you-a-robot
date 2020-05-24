package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentFindMatchBinding
import kotlinx.coroutines.launch

class FindMatchFragment : Fragment() {
    private val viewModel: FindMatchViewModel by activityViewModels()
    private lateinit var binding: FragmentFindMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(inflater,
             R.layout.fragment_find_match,container,false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultPlayerName = Utils.createDefaultRandomName()
        val playerName = sharedPref!!.getString("player_name",defaultPlayerName)

        viewModel.setPlayerName(playerName!!)
        searchButtonListener()
        cancelSearchButtonListener()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun searchButtonListener(){
        binding.apply {
        searchMatch.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.startMatchmaking().observe(viewLifecycleOwner, Observer { matchRoom ->
                    if (matchRoom.full){
                        val action = FindMatchFragmentDirections.actionFindMatchFragmentToMatchFragment(matchRoom.id,matchRoom.playerPos)
                        findNavController().navigate(action)}
                })
            }
            searchMatch.visibility = View.GONE
            cancelSearch.visibility = View.VISIBLE
            searchMatchProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun cancelSearchButtonListener(){
        binding.apply {
            cancelSearch.setOnClickListener {
                viewModel.unsubscribeToMatch()
                searchMatch.visibility = View.VISIBLE
                cancelSearch.visibility = View.GONE
                searchMatchProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        viewModel.unsubscribeToMatch()
        super.onDestroy()
    }
}
