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
    suspend fun insertWonder(wonder: CachedWonder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWonders(wonders: List<CachedWonder>)

    @Query("SELECT * FROM cached_wonders")
    fun getAllWonders(): Flow<List<CachedWonder>>

    @Query("SELECT * FROM cached_wonders WHERE id = :id")
    suspend fun getWonderById(id: String): CachedWonder?

    @Query("SELECT * FROM cached_wonders WHERE (categories) LIKE '%' || :category || '%'")
    fun getWonderByCategory(category: String): Flow<List<CachedWonder>>

    @Query(
        """
        SELECT * FROM cached_wonders 
        WHERE (:textQuery IS NULL OR name LIKE '%' || :textQuery || '%' 
        OR location LIKE '%' || :textQuery || '%' 
        OR summary LIKE '%' || :textQuery || '%' 
        OR buildYear LIKE '%' || :textQuery || '%')
        AND (:timePeriodQuery IS NULL OR timePeriod = :timePeriodQuery)
        AND (:categoryQuery IS NULL OR (categories) LIKE '%' || :categoryQuery || '%')
    """
    )
    fun getWondersBy(
        textQuery: String?,
        timePeriodQuery: String?,
        categoryQuery: String?
    ): Flow<List<CachedWonder>>
}
