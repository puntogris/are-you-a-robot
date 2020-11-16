package com.puntogris.areyouarobot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentPostGameBinding

class PostGameFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPostGameBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_game,container,false)

        binding.apply {
            gameFragment = this@PostGameFragment
            gameViewModel = viewModel

        }

        return binding.root
    }

    fun openSaveRankingDialog(){
        val dialog = CustomDialog(viewModel.score.value!!)
        dialog.show(requireActivity().supportFragmentManager, "ranking dialog")
    }

    fun playAgain(){
        findNavController().navigate(R.id.singlePlayerGameFragment)
    }
}
