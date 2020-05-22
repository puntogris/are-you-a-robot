package com.puntogris.areyouarobot.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentSinglePlayerGameBinding

class SinglePlayerGameFragment : Fragment() {

    private lateinit var binding: FragmentSinglePlayerGameBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_player_game,container,false)

        viewModel.initializeGame()
        listenToTextChanged()

        viewModel.apply {
            currentLetters.observe(viewLifecycleOwner, Observer {
                binding.lettersTextView.text = it
            })
            isTimeToGuess.observe(viewLifecycleOwner, Observer {
                if (it) {
                    guessTime()
                    showSoftKeyboard(binding.guessEditText)} else showLetters()
            })
            didPlayerLose.observe(viewLifecycleOwner, Observer {
                if (it) navigateToPostGameFragment()
            })
            score.observe(viewLifecycleOwner, Observer {
                binding.scoreTextView.text = it.toString()
            })
            globalTime.observe(viewLifecycleOwner, Observer {
                binding.timerTextView.text = it.toString()
            })
            progressBarStatus.observe(viewLifecycleOwner, Observer {
                binding.progressBar.progress = it
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
        binding.guessEditText.setText("")
        binding.progressBar.visibility = View.VISIBLE
        binding.lettersTextView.visibility = View.GONE
        binding.guessEditText.visibility = View.VISIBLE
    }

    private fun showLetters(){
        binding.progressBar.visibility = View.GONE
        binding.lettersTextView.visibility = View.VISIBLE
        binding.guessEditText.visibility = View.GONE
    }

    private fun navigateToPostGameFragment(){
        findNavController().navigate(R.id.action_singlePlayerGameFragment_to_postGameFragment)
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.welcomeFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }


}
