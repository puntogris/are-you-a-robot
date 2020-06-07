package com.puntogris.areyouarobot.ui

import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentSinglePlayerGameBinding
import com.puntogris.areyouarobot.utils.Utils

class SinglePlayerGameFragment : Fragment() {

    private lateinit var binding: FragmentSinglePlayerGameBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_player_game,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.gameViewModel = viewModel

        viewModel.apply {
            initializeGame()
            listenToTextChanged()
            isTimeToGuess.observe(viewLifecycleOwner, Observer { guessTime ->
                if (guessTime) {
                    guessTime()
                    Utils.showSoftKeyboard(binding.guessEditText, requireActivity())} else showLetters()
            })
            didPlayerLose.observe(viewLifecycleOwner, Observer { playerLost ->
                if (playerLost) navigateToPostGame()
            })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun listenToTextChanged(){
        binding.guessEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString() == viewModel.currentLetters.value) {
                viewModel.playerWon()
            }
        }
    }

    private fun guessTime(){
        binding.apply {
            guessEditText.setText("")
            lettersTextView.visibility = View.GONE
            guessEditText.visibility = View.VISIBLE
        }
    }

    private fun showLetters(){
        binding.apply {
            lettersTextView.visibility = View.VISIBLE
            guessEditText.visibility = View.GONE
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
