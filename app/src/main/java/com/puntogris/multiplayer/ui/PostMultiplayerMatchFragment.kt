package com.puntogris.multiplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentPostMultiplayerMatchBinding
import com.puntogris.areyouarobot.model.Match
import com.puntogris.areyouarobot.model.Match.Winner
import com.puntogris.areyouarobot.utils.gone
import com.puntogris.areyouarobot.utils.viewBinding
import com.puntogris.areyouarobot.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostMultiplayerMatchFragment : Fragment(R.layout.fragment_post_multiplayer_match) {

    private val args: PostMultiplayerMatchFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentPostMultiplayerMatchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateWinnerUi(args.match)
        binding.playerOneName.text = args.match.playerOneName
        binding.playerTwoName.text = args.match.playerTwoName
        binding.playerOneScore.text = args.match.playerOneScore.toString()
        binding.playerTwoScore.text = args.match.playerTwoScore.toString()
        binding.shareResults.setOnClickListener {
            onShareResultsClicked()
        }
    }

    private fun updateWinnerUi(match: Match) {
        with(binding) {
            when (match.result()) {
                Winner.PlayerOne -> {
                    playerWinner.text = match.playerOneName
                    playerLoser.text = match.playerTwoName
                }

                Winner.PlayerTwo -> {
                    playerWinner.text = match.playerTwoName
                    playerLoser.text = match.playerOneName
                }

                Winner.Draw -> {
                    robotPlayerText.gone()
                    playerWinner.gone()
                    playerLoser.gone()
                    humanPlayerText.gone()
                    drawResult.visible()
                }
            }
        }
    }

    fun onShareResultsClicked() {
        binding.shareResults.setOnClickListener {
            val shareText = getString(
                R.string.share_match_text,
                args.match.playerOneName,
                args.match.playerOneScore,
                args.match.playerTwoName,
                args.match.playerTwoScore

            )
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, shareText)
            startActivity(shareIntent)
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
