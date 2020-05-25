package com.puntogris.areyouarobot.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.areyouarobot.databinding.RankingEntryBinding
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.diffcallback.ItemCallback
import com.puntogris.areyouarobot.diffcallback.QueryItem

class RankingsRecyclerViewAdapter:
    ListAdapter<QueryItem<RankingEntry>, RankingsRecyclerViewAdapter.ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding:RankingEntryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: QueryItem<RankingEntry>){
            binding.entry = item.item
            binding.entryNumberIndex = item.id.toInt() +1
        }

        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RankingEntryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}