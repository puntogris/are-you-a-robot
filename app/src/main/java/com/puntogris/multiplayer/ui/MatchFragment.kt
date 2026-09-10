package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentMatchBinding
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.viewBinding
import com.puntogris.areyouarobot.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchFragment : Fragment(R.layout.fragment_match) {

    private val viewModel: MatchViewModel by activityViewModels()
    private val args: MatchFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentMatchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initializeGame(args.matchId, args.playerPos)
        viewModel.getMatchData(args.matchId)
        listenToTextChanged()
        collectViewModelState()
    }

    private fun collectViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.globalTime.collect {
                        binding.matchTimer.text = it.toString()
                    }
                }
                launch {
                    viewModel.matchInfo.collect { match ->
                        match ?: return@collect
                        binding.playerName.text = match.playerOneName
                        binding.opponentPlayerName.text = match.playerTwoName
                        binding.playerScore.text = match.playerOneScore.toString()
                        binding.opponentPlayerScore.text = match.playerTwoScore.toString()
                    }
                }
                launch {
                    viewModel.currentLetters.collect {
                        binding.lettersTextView.text = it
                    }
                }
                launch {
                    viewModel.progressBarStatus.collect {
                        binding.progressBar.progress = it
                    }
                }
                launch {
                    viewModel.isTimeToGuess.collect { guessTime ->
                        if (guessTime) {
                            guessTime()
                            Utils.showSoftKeyboard(binding.guessEditText, requireActivity())
                        } else {
                            showLetters()
                        }
                    }
                }
                launch {
                    viewModel.gameEnded.collect { gameEnded ->
                        if (gameEnded) navigateToPostGameFragment()
                    }
                }
            }
        }
    }

    private fun guessTime() {
        binding.apply {
            guessEditText.setText("")
            lettersTextView.gone()
            progressBar.visible()
            guessEditText.visible()
        }
    }

    private fun showLetters() {
        binding.apply {
            lettersTextView.visible()
            progressBar.gone()
            guessEditText.gone()
        }
    }

    private fun navigateToPostGameFragment() {
        val match = viewModel.matchInfo.value ?: return
        viewModel.gameEnded()
        val action = MatchFragmentDirections.actionMatchFragmentToPostMultiplayerMatchFragment(
            args.playerPos,
            match
        )
        findNavController().navigate(action)
    }

    private fun listenToTextChanged() {
        binding.guessEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString() == viewModel.currentLetters.value) {
                viewModel.guessCorrect()
            }
        }
    }

    override fun onDestroy() {
        viewModel.gameEnded()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(com.puntogris.areyouarobot.R.id.welcomeFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

}
