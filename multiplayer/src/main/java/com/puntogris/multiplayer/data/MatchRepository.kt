package com.puntogris.multiplayer.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.puntogris.areyouarobot.utils.Constants.MATCHES_COLLECTION
import com.puntogris.areyouarobot.utils.Constants.PLAYER_FIELD
import com.puntogris.areyouarobot.utils.Constants.START_MATCHMAKING_FUNCTION
import com.puntogris.areyouarobot.utils.Constants.UNSUBSCRIBE_TO_MATCH_FUNCTION
import com.puntogris.areyouarobot.utils.SimpleResult
import com.puntogris.multiplayer.livedata.FirestoreDocumentLiveData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MatchRepository @Inject constructor(): IMatchRepository{

    private val firestore = Firebase.firestore
    private val functions = FirebaseFunctions.getInstance()

    override suspend fun getMatchFirestore(playerName:String): FirestoreDocumentLiveData {
        val matchId = functions
            .getHttpsCallable(START_MATCHMAKING_FUNCTION)
            .call(PLAYER_FIELD to playerName)
            .await()
        val ref = firestore.collection(MATCHES_COLLECTION).document(matchId.data.toString())
        return FirestoreDocumentLiveData(ref)
    }

    override suspend fun unsubscribeToMatchFirestore(playerName: String): SimpleResult {
        return try {
            functions
                .getHttpsCallable(UNSUBSCRIBE_TO_MATCH_FUNCTION)
                .call(PLAYER_FIELD to playerName)
                .await()
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    override fun getMatchDataFirestore(matchId: String): FirestoreDocumentLiveData {
        val ref = firestore.collection(MATCHES_COLLECTION).document(matchId)
        return FirestoreDocumentLiveData(ref)
    }

    override fun incrementScorePlayerFirestore(playerPos: String, matchId: String) {
        firestore.collection(MATCHES_COLLECTION)
            .document(matchId)
            .update("$playerPos.score", FieldValue.increment(1))
    }
}