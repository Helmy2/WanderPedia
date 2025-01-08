package com.example.wanderpedia.core.domain.model


data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val imageUrl: String = "",
    val isAnonymous: Boolean = true
)