package com.puntogris.multiplayer.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.puntogris.areyouarobot.ui.base.BaseFragment
import com.puntogris.areyouarobot.utils.SimpleResult
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.visible
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentFindMatchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FindMatchFragment : BaseFragment<FragmentFindMatchBinding>(R.layout.fragment_find_match) {

    private val viewModel: FindMatchViewModel by activityViewModels()

    override fun initializeViews() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        subscribeMatchState()
    }

    private fun subscribeMatchState() {
        viewModel.isSearching.observe(viewLifecycleOwner) { isSearching ->
            if (isSearching) startMatchSearch() else unsubscribeToMatch()
        }
    }

    private fun startMatchSearch() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.startMatchmaking().observe(viewLifecycleOwner) { matchRoom ->
                if (matchRoom.full) {
                    val action = FindMatchFragmentDirections
                        .actionFindMatchFragmentToMatchFragment(matchRoom.id, matchRoom.playerPos)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun unsubscribeToMatch() {
        lifecycleScope.launch {
            val message = when (viewModel.unsubscribeToMatchDatabase()) {
                SimpleResult.Failure -> R.string.snack_search_cancelled
                SimpleResult.Success -> R.string.snack_search_started
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        unsubscribeToMatch()
        super.onDestroy()
    }

    override fun onPause() {
        unsubscribeToMatch()
        super.onPause()
    }
}
