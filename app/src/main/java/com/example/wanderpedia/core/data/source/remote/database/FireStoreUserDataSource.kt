package com.example.wanderpedia.core.data.source.remote.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FireStoreUserDataSource @Inject constructor(
    val firestore: FirebaseFirestore,
    val auth: FirebaseAuth,
) : RemoteUserDataSource {


    fun getReference(): CollectionReference? {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            return firestore.collection(USERS).document(userId).collection(FAVORITES)
        }
        return null
    }

    companion object {
        private const val ID = "id"
        private const val USERS = "users"
        private const val FAVORITES = "favorites"
    }

    override suspend fun addFavoriteWonder(id: String) {
        val reference = getReference()
        if (reference == null)
            throw Exception("No user logged in")

        reference.add(hashMapOf(ID to id)).await()
    }

    override suspend fun getFavoriteWondersId(): List<String> {
        val reference = getReference()
        if (reference == null)
            return emptyList()

        val querySnapshot = reference.get().await()

        val list: MutableList<String> = mutableListOf()
        for (document in querySnapshot.documents) {
            val id = document[ID].toString()
            id.let { list.add(it) }
        }
        return list

    }

    override suspend fun removeFavoriteWonder(id: String) {
        val reference = getReference()
        if (reference == null)
            throw Exception("No user logged in")

        val personQuery = reference.whereEqualTo(ID, id).get().await()
        if (personQuery.documents.isNotEmpty()) {
            for (document in personQuery) {
                reference.document(document.id).delete().await()
            }
        }
    }
}