package com.example.wanderpedia.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Wonder(
    val id: String,
    val location: String,
    val name: String,
    val imageUrl: String,
)