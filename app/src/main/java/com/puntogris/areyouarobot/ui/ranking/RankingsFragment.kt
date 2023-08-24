package com.puntogris.areyouarobot.ui.ranking

import androidx.fragment.app.viewModels
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentRankingsBinding
import com.puntogris.areyouarobot.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingsFragment : BaseFragment<FragmentRankingsBinding>(R.layout.fragment_rankings) {

    private val viewModel: RankingsViewModel by viewModels()

    override fun initializeViews() {
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
