package com.puntogris.multiplayer.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.puntogris.multiplayer.R
import com.puntogris.multiplayer.databinding.FragmentPostMultiplayerMatchBinding


class PostMultiplayerMatchFragment : Fragment() {
private val args:PostMultiplayerMatchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentPostMultiplayerMatchBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_multiplayer_match, container,false)

        binding.args = args

        if (getWinner(args.playerOneScore,args.playerTwoScore)){
            binding.playerWinner.text = args.playerOneName
            binding.playerLosser.text = args.playerTwoName
        }else{
            binding.playerWinner.text = args.playerTwoName
            binding.playerLosser.text = args.playerOneName

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getWinner(playerOne:Int, playerTwo: Int) = playerOne > playerTwo

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(com.puntogris.areyouarobot.R.id.welcomeFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }
}