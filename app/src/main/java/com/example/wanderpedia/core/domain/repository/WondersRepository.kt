package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.Wonder

interface WondersRepository {
    suspend fun getWonders(
        name: String,
        location: String,
        category: Category
    ): Resource<List<Wonder>>
}