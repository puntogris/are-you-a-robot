package com.puntogris.areyouarobot.data

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.areyouarobot.livedata.FirestoreQueryLiveData
import com.puntogris.areyouarobot.model.RankingEntry
import javax.inject.Inject

class Repository @Inject constructor() : IRepository{
    private val firestore = Firebase.firestore

    override fun saveScoreFirestore(rankingEntry: RankingEntry) {
        firestore.collection("rankings").document().set(rankingEntry)
    }

    override fun getRankingFirestore(): FirestoreQueryLiveData {
        val ref = firestore.collection("rankings").orderBy("score",Query.Direction.DESCENDING).limit(20L)
        return FirestoreQueryLiveData(ref)
    }

}