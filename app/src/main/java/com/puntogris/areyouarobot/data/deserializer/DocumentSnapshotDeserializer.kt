package com.puntogris.areyouarobot.data.deserializer

import com.google.firebase.firestore.DocumentSnapshot

interface DocumentSnapshotDeserializer<T> : Deserializer<DocumentSnapshot, T>
