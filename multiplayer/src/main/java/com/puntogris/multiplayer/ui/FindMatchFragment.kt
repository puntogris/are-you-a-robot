package com.puntogris.multiplayer.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.ui.base.BaseFragment
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.visible
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentFindMatchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FindMatchFragment : BaseFragment<FragmentFindMatchBinding>(R.layout.fragment_find_match) {

    private val viewModel: FindMatchViewModel by activityViewModels()

    @Inject
    lateinit var sharedPref: SharedPref

    override fun initializeViews() {

        viewModel.setPlayerName(sharedPref.getPlayerName())
        searchButtonListener()
        cancelSearchButtonListener()
    }

    private fun searchButtonListener(){
        binding.apply {
            searchMatch.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.startMatchmaking().observe(viewLifecycleOwner) { matchRoom ->
                        if (matchRoom.full){
                            val action = FindMatchFragmentDirections.actionFindMatchFragmentToMatchFragment(matchRoom.id, matchRoom.playerPos)
                            findNavController().navigate(action)}
                    }
                }
                searchMatch.gone()
                cancelSearch.visible()
                searchMatchProgressBar.visible()
            }
        }
    }

    private fun cancelSearchButtonListener(){
        binding.apply {
            cancelSearch.setOnClickListener {
                viewModel.unsubscribeToMatch()
                searchMatch.visible()
                cancelSearch.gone()
                searchMatchProgressBar.gone()
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
