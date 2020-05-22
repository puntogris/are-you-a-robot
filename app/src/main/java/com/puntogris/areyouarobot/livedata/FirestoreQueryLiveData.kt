package com.puntogris.areyouarobot.livedata

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*


class FirestoreQueryLiveData(private val query: Query)
    : MutableLiveData<List<DocumentSnapshot?>>(), EventListener<QuerySnapshot>{

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
        val documents : List<DocumentSnapshot> = snapshot!!.documents
        postValue(documents)
    }

}

//
//class FirestoreQueryLiveData(private val query: Query)
//    : MutableLiveData<List<DocumentSnapshot?>>(), OnSuccessListener<QuerySnapshot>,
//    OnFailureListener {
//
//    override fun onActive() {
//        query.get().addOnSuccessListener(this)
//    }
//
//    override fun onSuccess(snap: QuerySnapshot?) {
//        postValue(snap!!.documents)
//    }
//
//    override fun onFailure(p0: Exception) {
//        throw p0
//    }
//
//}
