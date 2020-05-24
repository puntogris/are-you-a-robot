package com.puntogris.areyouarobot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.FragmentRankingBinding
import com.puntogris.areyouarobot.utils.Utils

class RankingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentRankingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_ranking,container, false)
        val viewModel: RankingViewModel by activityViewModels()
        //RecyclerView
        val manager = LinearLayoutManager(activity,  LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewRanking.layoutManager = manager
        val adapter = RankingRecyclerViewAdapter()
        binding.recyclerViewRanking.adapter = adapter
        viewModel.getRanking().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}
