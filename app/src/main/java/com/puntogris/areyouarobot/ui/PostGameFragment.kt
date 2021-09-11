package com.puntogris.areyouarobot.ui

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentPostGameBinding
import com.puntogris.areyouarobot.ui.base.BaseFragment

class PostGameFragment : BaseFragment<FragmentPostGameBinding>(R.layout.fragment_post_game) {

    private val viewModel: GameViewModel by activityViewModels()

    override fun initializeViews() {
        binding.apply {
            gameFragment = this@PostGameFragment
            gameViewModel = viewModel
        }
    }

    fun openSaveRankingDialog(){
        val dialog = CustomDialog(viewModel.score.value!!)
        dialog.show(requireActivity().supportFragmentManager, "ranking dialog")
    }

    fun playAgain(){
        findNavController().navigate(R.id.singlePlayerGameFragment)
    }
}
