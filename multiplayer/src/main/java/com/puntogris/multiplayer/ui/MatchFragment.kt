package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.puntogris.areyouarobot.utils.Utils
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentMatchBinding

class MatchFragment : Fragment() {
    private val viewModel: MatchViewModel by activityViewModels()
    private val args:MatchFragmentArgs by navArgs()
    private lateinit var binding: FragmentMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_match, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.matchViewModel = viewModel

        viewModel.apply {
            initializeGame(args.matchId,args.playerPos)
            listenToTextChanged()
            isTimeToGuess.observe(viewLifecycleOwner, Observer { guessTime ->
                if (guessTime) {
                    guessTime()
                    Utils.showSoftKeyboard(binding.guessEditText,requireActivity())} else showLetters()
            })

        }

        viewModel.getMatchData(args.matchId).observe(viewLifecycleOwner, Observer {
            binding.match = it
        })

        viewModel.isTimeToGuess.observe(viewLifecycleOwner, Observer { guessTime ->
            if (guessTime) guessTime() else showLetters()
        })

        viewModel.gameEnded.observe(viewLifecycleOwner, Observer {
            if (it) navigateToPostGameFragment()
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun guessTime(){
        binding.guessEditText.setText("")
        binding.lettersTextView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.guessEditText.visibility = View.VISIBLE
    }

    private fun showLetters(){
        binding.lettersTextView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.guessEditText.visibility = View.GONE
    }

    private fun navigateToPostGameFragment(){
        viewModel.gameEnded()
        val matchInfo = viewModel.matchInfo.value
        val action = MatchFragmentDirections
            .actionMatchFragmentToPostMultiplayerMatchFragment(
                matchInfo!!.playerOneName,
                matchInfo.playerTwoName,
                matchInfo.playerOneScore,
                matchInfo.playerTwoScore)
        findNavController().navigate(action)
    }

    private fun listenToTextChanged(){
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




