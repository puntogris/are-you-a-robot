package com.puntogris.multiplayer.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.puntogris.multiplayer.livedata.FirestoreDocumentLiveData
import kotlinx.coroutines.tasks.await

class MatchRepository :IMatchRepository{
    private val firestore = Firebase.firestore
    private val functions = FirebaseFunctions.getInstance()

    override suspend fun getMatchFirestore(playerName:String): FirestoreDocumentLiveData {
        val data = hashMapOf("player" to playerName)
        val matchId = functions
            .getHttpsCallable("startMatchmaking")
            .call(data)
            .await()
        val ref = firestore.collection("matches").document(matchId.data.toString())
        return FirestoreDocumentLiveData(ref)
    }

    override suspend fun unsubscribeToMatchFirestore(playerName: String) {
        val data = hashMapOf("player" to playerName)
        functions
            .getHttpsCallable("unsubscribeToMatch")
            .call(data)
            .await()
    }

    override fun getMatchDataFirstore(matchId: String): FirestoreDocumentLiveData {
        val ref = firestore.collection("matches").document(matchId)
        return FirestoreDocumentLiveData(ref)
    }


}