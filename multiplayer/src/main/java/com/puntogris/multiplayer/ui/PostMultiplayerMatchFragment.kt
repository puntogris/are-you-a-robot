package com.puntogris.multiplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.puntogris.areyouarobot.ui.base.BaseFragment
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentPostMultiplayerMatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostMultiplayerMatchFragment :
    BaseFragment<FragmentPostMultiplayerMatchBinding>(R.layout.fragment_post_multiplayer_match) {

    private val args:PostMultiplayerMatchFragmentArgs by navArgs()

    override fun initializeViews() {
        binding.matchData = args
        getWinner()

        binding.shareResults.setOnClickListener {
            val shareText = "I just played a game in Are you a robot where, ${args.playerOneName} scored a ${args.playerOneScore} and ${args.playerTwoName} scored a ${args.playerTwoScore}."

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, shareText)
            startActivity(shareIntent)
        }
    }

    private fun getWinner() {
        when {
            args.playerOneScore > args.playerTwoScore -> {
                binding.playerWinner.text = args.playerOneName
                binding.playerLosser.text = args.playerTwoName
            }
            args.playerOneScore < args.playerTwoScore -> {
                binding.playerWinner.text = args.playerTwoName
                binding.playerLosser.text = args.playerOneName
            }
            else -> {
                binding.robotPlayerText.visibility = View.GONE
                binding.playerWinner.visibility = View.GONE
                binding.playerLosser.visibility = View.GONE
                binding.humanPlayerText.visibility = View.GONE
                binding.drawResult.visibility = View.VISIBLE
            }
        }
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