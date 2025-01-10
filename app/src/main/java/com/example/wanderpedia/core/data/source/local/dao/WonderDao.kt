package com.example.wanderpedia.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wanderpedia.core.data.source.local.model.CachedWonder
import kotlinx.coroutines.flow.Flow

@Dao
interface WonderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWonders(wonders: List<CachedWonder>)

    @Query("SELECT * FROM cached_wonders")
    fun getAllWonders(): Flow<List<CachedWonder>>

    @Query("SELECT * FROM cached_wonders WHERE id = :id")
    suspend fun getWonderById(id: String): CachedWonder?

    @Query("SELECT * FROM cached_wonders WHERE :category IN (categories)")
    fun getWonderByCategory(category: String): Flow<List<CachedWonder>>

    @Query(
        """
        SELECT * FROM cached_wonders 
        WHERE (:nameQuery IS NULL OR name LIKE '%' || :nameQuery || '%')
        AND (:locationQuery IS NULL OR location LIKE '%' || :locationQuery || '%')
        AND (:timePeriodQuery IS NULL OR timePeriod = :timePeriodQuery)
        AND (:categoryQuery IS NULL OR :categoryQuery IN (categories))
    """
    )
    fun getWondersBy(
        nameQuery: String?,
        locationQuery: String?,
        timePeriodQuery: String?,
        categoryQuery: String?
    ): Flow<List<CachedWonder>>
}