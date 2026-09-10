package com.puntogris.areyouarobot.data

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.data.deserializer.RankingEntryDeserializer
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.diffcallback.QueryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object FirestoreQueryRankingEntryTransformation {

    fun transform(flow: Flow<List<DocumentSnapshot>>): Flow<List<QueryItem<RankingEntry>>> {
        return flow.map { snapshots ->
            snapshots.mapIndexed { index, entry ->
                val data =
                    RankingEntryDeserializer.deserialize(
                        entry
                    )
                object :
                    QueryItem<RankingEntry> {
                    override val item: RankingEntry
                        get() = data
                    override val id: String
                        get() = index.toString()
                }
            }
        }
    }

}
