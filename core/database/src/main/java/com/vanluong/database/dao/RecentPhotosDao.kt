package com.vanluong.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vanluong.database.entity.PhotoEntity

/**
 * Created by van.luong
 * on 08,May,2025
 *
 * Data Access Object (DAO) for managing recent photos in the database.
 */
@Dao
interface RecentPhotosDao {
    @Query("SELECT * FROM recentPhotos WHERE id = :id")
    suspend fun getPhotoById(id: String): PhotoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Query(
        """
        DELETE FROM recentPhotos 
        WHERE id NOT IN (
            SELECT id FROM recentPhotos ORDER BY timestamp DESC LIMIT 50
        )
    """
    )
    suspend fun enforceLRULimit()
}