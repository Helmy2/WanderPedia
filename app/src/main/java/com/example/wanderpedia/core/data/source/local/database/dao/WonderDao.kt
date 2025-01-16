package com.example.wanderpedia.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wanderpedia.core.data.source.local.database.model.CachedWonder

@Dao
interface WonderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWonder(wonder: CachedWonder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWonders(wonders: List<CachedWonder>)

    @Query("SELECT * FROM cached_wonders")
    fun getAllWonders(): List<CachedWonder>

    @Query("SELECT * FROM cached_wonders WHERE id = :id")
    suspend fun getWonderById(id: String): CachedWonder?

    @Query("SELECT * FROM cached_wonders WHERE (categories) LIKE '%' || :category || '%'")
    fun getWonderByCategory(category: String): List<CachedWonder>

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
    ): List<CachedWonder>

    @Query("UPDATE cached_wonders SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateWonderFavorite(id: String, isFavorite: Boolean)
}
