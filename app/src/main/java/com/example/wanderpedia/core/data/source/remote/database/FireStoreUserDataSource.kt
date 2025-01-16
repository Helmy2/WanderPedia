package com.example.wanderpedia.core.data.source.remote.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FireStoreUserDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    auth: FirebaseAuth,
) : RemoteUserDataSource {

    var reference: CollectionReference? = null

    init {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            reference = firestore.collection(USERS).document(userId).collection(FAVORITES)
        }
    }

    companion object {
        private const val ID = "id"
        private const val USERS = "users"
        private const val FAVORITES = "favorites"
    }

    override suspend fun addFavoriteWonder(id: String) {
        if (reference == null)
            throw Exception("No user logged in")

        reference!!.add(hashMapOf(ID to id)).await()
    }

    override suspend fun getFavoriteWondersId(): List<String> {
        if (reference == null)
            return emptyList()

        val querySnapshot = reference!!.get().await()

        val list: MutableList<String> = mutableListOf()
        for (document in querySnapshot.documents) {
            val id = document[ID].toString()
            id.let { list.add(it) }
        }
        return list

    }

    override suspend fun removeFavoriteWonder(id: String) {
        if (reference == null)
            throw Exception("No user logged in")

        val personQuery = reference!!.whereEqualTo(ID, id).get().await()
        if (personQuery.documents.isNotEmpty()) {
            for (document in personQuery) {
                reference!!.document(document.id).delete().await()
            }
        }
    }
}