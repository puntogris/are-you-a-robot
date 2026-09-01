package com.puntogris.areyouarobot.ui.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentPostGameBinding
import com.puntogris.areyouarobot.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostGameFragment : Fragment(R.layout.fragment_post_game) {

    private val viewModel: GameViewModel by activityViewModels()
    private val binding by viewBinding(FragmentPostGameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tryAgainButtonPostGame.setOnClickListener {
            playAgain()
        }
        binding.saveScore.setOnClickListener {
            openSaveRankingDialog()
        }
        viewModel.score.observe(viewLifecycleOwner) {
            binding.scorePostGame.text = it.toString()
        }
        viewModel.globalTime.observe(viewLifecycleOwner) {
            binding.timerPostGame.text = it.toString()
        }
    }


    fun openSaveRankingDialog() {
        val action = PostGameFragmentDirections.actionPostGameFragmentToSaveRankingDialog(
            viewModel.score.value!!
        )
        findNavController().navigate(action)
    }

    fun playAgain() {
        findNavController().navigate(R.id.singlePlayerGameFragment)
    }
}
