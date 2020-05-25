package com.puntogris.areyouarobot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentRankingsBinding

class RankingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRankingsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_rankings,container, false)
        val viewModel: RankingsViewModel by activityViewModels()
        //RecyclerView
        val manager = LinearLayoutManager(activity,  LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewRanking.layoutManager = manager
        val adapter = RankingsRecyclerViewAdapter()
        binding.recyclerViewRanking.adapter = adapter
        viewModel.getRanking().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}
