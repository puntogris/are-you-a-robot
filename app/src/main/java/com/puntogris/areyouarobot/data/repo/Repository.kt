package com.puntogris.areyouarobot.data.repo

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.areyouarobot.livedata.FirestoreQueryLiveData
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.Constants.RANKINGS_COLLECTION
import com.puntogris.areyouarobot.utils.Constants.SCORE_FIELD
import com.puntogris.areyouarobot.utils.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor() : IRepository {

    private val firestore = Firebase.firestore

    override suspend fun saveScoreFirestore(rankingEntry: RankingEntry): SimpleResult =
        withContext(Dispatchers.IO) {
            try {
                firestore
                    .collection(RANKINGS_COLLECTION)
                    .add(rankingEntry)
                    .await()

                SimpleResult.Success
            } catch (e: Exception) {
                SimpleResult.Failure
            }
        }

    override fun getRankingFirestore(): FirestoreQueryLiveData {
        val ref = firestore
            .collection(RANKINGS_COLLECTION)
            .orderBy(SCORE_FIELD, Query.Direction.DESCENDING)
            .limit(20)
        return FirestoreQueryLiveData(ref)
    }
}