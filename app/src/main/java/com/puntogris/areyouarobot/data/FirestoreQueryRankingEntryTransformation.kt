package com.puntogris.areyouarobot.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.data.deserializer.RankingEntryDeserializer
import com.puntogris.areyouarobot.livedata.FirestoreQueryLiveData
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.diffcallback.QueryItem

object FirestoreQueryRankingEntryTransformation {

    fun transform(liveData: FirestoreQueryLiveData): LiveData<List<QueryItem<RankingEntry>>>  {
        return Transformations.map(liveData) { snap: List<DocumentSnapshot?> ->
            snap.map { entry ->
                val data =
                    RankingEntryDeserializer.deserialize(
                        entry
                    )
                object :
                    QueryItem<RankingEntry> {
                    override val item: RankingEntry
                        get() = data
                    override val id: String
                        get() = snap.indexOf(entry).toString()
                }
            }
        }
    }

}