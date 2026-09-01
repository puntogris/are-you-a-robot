package com.puntogris.areyouarobot.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.areyouarobot.databinding.RankingEntryBinding
import com.puntogris.areyouarobot.diffcallback.QueryItem
import com.puntogris.areyouarobot.model.RankingEntry

class RankingViewHolder(private val binding: RankingEntryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: QueryItem<RankingEntry>) {
        binding.entryNumber.text = item.id.toInt().inc().toString()
        binding.usernameRankingAdapter.text = item.item.playerName
        binding.scoreRankingAdapter.text = item.item.score.toString()
    }

    companion object {
        fun from(parent: ViewGroup): RankingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RankingEntryBinding.inflate(layoutInflater, parent, false)
            return RankingViewHolder(binding)
        }
    }
}