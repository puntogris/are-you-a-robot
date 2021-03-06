package com.puntogris.multiplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentFindMatchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FindMatchFragment : Fragment() {
    private val viewModel: FindMatchViewModel by activityViewModels()
    private lateinit var binding: FragmentFindMatchBinding
    @Inject
    lateinit var sharedPref: SharedPref


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_find_match,container,false)

        val playerName = sharedPref.getPlayerName()

        viewModel.setPlayerName(playerName)
        searchButtonListener()
        cancelSearchButtonListener()

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

    override fun onPause() {
        viewModel.unsubscribeToMatch()
        super.onPause()
    }
}
