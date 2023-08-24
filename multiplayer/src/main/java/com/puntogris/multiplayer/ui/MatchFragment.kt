package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.puntogris.areyouarobot.ui.base.BaseFragment
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.visible
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentMatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchFragment : BaseFragment<FragmentMatchBinding>(R.layout.fragment_match) {

    private val viewModel: MatchViewModel by activityViewModels()
    private val args: MatchFragmentArgs by navArgs()

    override fun initializeViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.matchViewModel = viewModel

        with(viewModel) {

            initializeGame(args.matchId, args.playerPos)
            listenToTextChanged()

            isTimeToGuess.observe(viewLifecycleOwner) { guessTime ->
                if (guessTime) {
                    guessTime()
                    Utils.showSoftKeyboard(binding.guessEditText, requireActivity())
                } else showLetters()
            }

            getMatchData(args.matchId).observe(viewLifecycleOwner) { match ->
                binding.match = match
            }

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




