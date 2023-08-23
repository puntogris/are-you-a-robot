package com.puntogris.areyouarobot.ui.ranking

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.diffcallback.ItemCallback
import com.puntogris.areyouarobot.diffcallback.QueryItem

class RankingsAdapter: ListAdapter<QueryItem<RankingEntry>, RankingViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        return RankingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}