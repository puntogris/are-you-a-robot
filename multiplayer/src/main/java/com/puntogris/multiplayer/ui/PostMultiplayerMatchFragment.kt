package com.puntogris.multiplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentPostMultiplayerMatchBinding

class PostMultiplayerMatchFragment : Fragment() {
    private val args:PostMultiplayerMatchFragmentArgs by navArgs()
    private lateinit var binding: FragmentPostMultiplayerMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_multiplayer_match, container,false)

        binding.args = args
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

        return binding.root
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