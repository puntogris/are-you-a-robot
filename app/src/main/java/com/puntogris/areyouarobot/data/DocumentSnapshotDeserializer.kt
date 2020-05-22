package com.puntogris.areyouarobot.data

import com.google.firebase.firestore.DocumentSnapshot

interface DocumentSnapshotDeserializer<T> :
    Deserializer<DocumentSnapshot, T>
