package com.puntogris.multiplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentMatchBinding

class MatchFragment : Fragment() {
    private val viewModel: MatchViewModel by activityViewModels()
    private val args:MatchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMatchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_match, container, false)

        viewModel.initGame()

        viewModel.getMatchData(args.matchId).observe(viewLifecycleOwner, Observer {
            binding.opponentPlayerName.text = it.playerOneName
            binding.opponentPlayerScore.text = it.playerOneScore.toString()
            binding.playerName.text = it.playerTwoName
            binding.playerScore.text = it.playerTwoScore.toString()
        })

        viewModel.globalTime.observe(viewLifecycleOwner, Observer {
            binding.matchTimer.text = it.toString()
        })


        // Inflate the layout for this fragment
        return binding.root
    }

}
