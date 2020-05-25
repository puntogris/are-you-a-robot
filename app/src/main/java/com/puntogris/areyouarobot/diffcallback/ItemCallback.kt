package com.puntogris.areyouarobot.diffcallback

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

open class ItemCallback<T> : DiffUtil.ItemCallback<QueryItem<T>>() {
    override fun areItemsTheSame(oldItem: QueryItem<T>, newItem: QueryItem<T>): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")  // equals() is OK for data classes
    override fun areContentsTheSame(oldItem: QueryItem<T>, newItem: QueryItem<T>): Boolean {
        return oldItem.item == newItem.item
    }
}