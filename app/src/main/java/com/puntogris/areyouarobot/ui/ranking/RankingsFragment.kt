package com.puntogris.areyouarobot.ui.ranking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentRankingsBinding
import com.puntogris.areyouarobot.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingsFragment : Fragment(R.layout.fragment_rankings) {

    private val binding by viewBinding(FragmentRankingsBinding::bind)

    private val viewModel: RankingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RankingsAdapter().let {
            binding.recyclerViewRanking.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: RankingsAdapter) {
        viewModel.getRankings().observe(viewLifecycleOwner) { rankingList ->
            adapter.submitList(rankingList)
        }
    }
}
