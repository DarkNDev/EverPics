package com.darkndev.everpics.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.darkndev.everpics.models.UnsplashPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface UnsplashPhotoDao {

    @Upsert
    suspend fun upsertPhotos(vararg photo: UnsplashPhoto)

    @Delete
    suspend fun deletePhotos(vararg photo: UnsplashPhoto)

    @Query("SELECT * from unsplash_photo WHERE id=:id")
    fun getPhoto(id: String): Flow<UnsplashPhoto?>

    @Query("SELECT * from unsplash_photo")
    fun getPhotos(): Flow<List<UnsplashPhoto>>
}