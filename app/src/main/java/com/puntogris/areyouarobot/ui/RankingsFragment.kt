package com.puntogris.areyouarobot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentRankingsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RankingsFragment : Fragment() {
    private val viewModel: RankingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRankingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_rankings, container, false)

        binding.recyclerViewRanking.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewRanking.adapter = RankingsRecyclerViewAdapter()
            .apply {
                viewModel.getRanking().observe(viewLifecycleOwner, Observer { rankingList ->
                    submitList(rankingList)
                })
            }

        return binding.root
    }

}
