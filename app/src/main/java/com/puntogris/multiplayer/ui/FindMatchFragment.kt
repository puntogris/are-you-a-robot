package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentFindMatchBinding
import com.puntogris.areyouarobot.utils.SimpleResult
import com.puntogris.areyouarobot.utils.viewBinding
import com.puntogris.multiplayer.utils.setSearchButtonState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FindMatchFragment : Fragment(R.layout.fragment_find_match) {

    private val viewModel: FindMatchViewModel by activityViewModels()
    private val binding by viewBinding(FragmentFindMatchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchMatch.setOnClickListener {
            viewModel.toggleQueueState()
        }

        subscribeMatchState()
    }

    private fun subscribeMatchState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isSearching.collect { isSearching ->
                        binding.searchMatchProgressBar.isVisible = isSearching
                        binding.searchMatch.setSearchButtonState(isSearching)
                    }
                }
                launch {
                    viewModel.isSearching.collectLatest { isSearching ->
                        if (isSearching) {
                            startMatchSearch()
                        } else {
                            unsubscribeToMatch()
                        }
                    }
                }
            }
        }
    }

    private suspend fun startMatchSearch() {
        viewModel.startMatchmaking().collectLatest { matchRoom ->
            if (matchRoom.full) {
                val action = FindMatchFragmentDirections
                    .actionFindMatchFragmentToMatchFragment(matchRoom.id, matchRoom.playerPos)
                findNavController().navigate(action)
            }
        }
    }

    private suspend fun unsubscribeToMatch() {
        val message = when (viewModel.unsubscribeToMatchDatabase()) {
            SimpleResult.Failure -> R.string.snack_search_cancelled
            SimpleResult.Success -> R.string.snack_search_started
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onPause() {
        lifecycleScope.launch {
            viewModel.unsubscribeToMatchDatabase()
        }
        super.onPause()
    }
}
