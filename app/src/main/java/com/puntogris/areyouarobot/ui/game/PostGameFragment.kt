package com.puntogris.areyouarobot.ui.game

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentPostGameBinding
import com.puntogris.areyouarobot.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostGameFragment : BaseFragment<FragmentPostGameBinding>(R.layout.fragment_post_game) {

    private val viewModel: GameViewModel by activityViewModels()

    override fun initializeViews() {
        binding.apply {
            gameFragment = this@PostGameFragment
            gameViewModel = viewModel
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
