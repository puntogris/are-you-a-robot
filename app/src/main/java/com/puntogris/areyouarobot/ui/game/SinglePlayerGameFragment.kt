package com.puntogris.areyouarobot.ui.game

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentSinglePlayerGameBinding
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.viewBinding
import com.puntogris.areyouarobot.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SinglePlayerGameFragment : Fragment(R.layout.fragment_single_player_game) {

    private val viewModel: GameViewModel by activityViewModels()
    private val binding by viewBinding(FragmentSinglePlayerGameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.currentLetters.observe(viewLifecycleOwner) {
            binding.lettersTextView.text = it.toString()
        }
        viewModel.score.observe(viewLifecycleOwner) {
            binding.scoreTextView.text = it.toString()
        }
        viewModel.globalTime.observe(viewLifecycleOwner) {
            binding.timerTextView.text = it.toString()
        }
        viewModel.progressBarStatus.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }
        viewModel.isTimeToGuess.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        with(viewModel) {

            initializeGame()
            listenToTextChanged()
            isTimeToGuess.observe(viewLifecycleOwner) { guessTime ->
                if (guessTime) {
                    guessTime()
                    Utils.showSoftKeyboard(binding.guessEditText, requireActivity())
                } else showLetters()
            }
            didPlayerLose.observe(viewLifecycleOwner) { playerLost ->
                if (playerLost) navigateToPostGame()
            }
        }
    }

    private fun listenToTextChanged() {
        binding.guessEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString() == viewModel.currentLetters.value) {
                viewModel.playerWon()
            }
        }
    }

    private fun guessTime() {
        binding.apply {
            guessEditText.setText("")
            lettersTextView.gone()
            guessEditText.visible()
        }
    }

    private fun showLetters() {
        binding.apply {
            lettersTextView.visible()
            guessEditText.gone()
        }
    }

    private fun navigateToPostGame() {
        findNavController().navigate(R.id.action_singlePlayerGameFragment_to_postGameFragment)
        viewModel.playerLost()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.welcomeFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroy() {
        viewModel.playerLost()
        super.onDestroy()
    }
}
