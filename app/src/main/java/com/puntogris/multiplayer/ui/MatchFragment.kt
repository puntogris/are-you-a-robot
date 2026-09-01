package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentMatchBinding
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.viewBinding
import com.puntogris.areyouarobot.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchFragment : Fragment(R.layout.fragment_match) {

    private val viewModel: MatchViewModel by activityViewModels()
    private val args: MatchFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentMatchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.globalTime.observe(viewLifecycleOwner) {
            binding.matchTimer.text = it.toString()
        }
        viewModel.matchInfo.observe(viewLifecycleOwner) {
            binding.playerName.text = it.playerOneName
            binding.opponentPlayerName.text = it.playerTwoName
            binding.playerScore.text = it.playerOneScore.toString()
            binding.opponentPlayerScore.text = it.playerTwoScore.toString()
        }
        viewModel.currentLetters.observe(viewLifecycleOwner) {
            binding.lettersTextView.text = it
        }
        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }

        with(viewModel) {

            initializeGame(args.matchId, args.playerPos)
            listenToTextChanged()

            isTimeToGuess.observe(viewLifecycleOwner) { guessTime ->
                if (guessTime) {
                    guessTime()
                    Utils.showSoftKeyboard(binding.guessEditText, requireActivity())
                } else showLetters()
            }

            getMatchData(args.matchId)

            isTimeToGuess.observe(viewLifecycleOwner) { guessTime ->
                if (guessTime) guessTime() else showLetters()
            }

            gameEnded.observe(viewLifecycleOwner) { gameEnded ->
                if (gameEnded) navigateToPostGameFragment()
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
        viewModel.gameEnded()
        val action = MatchFragmentDirections.actionMatchFragmentToPostMultiplayerMatchFragment(
            args.playerPos,
            viewModel.matchInfo.value!!
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

