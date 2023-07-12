package com.darkndev.everpics.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.darkndev.everpics.models.UnsplashPhoto

@Database(entities = [UnsplashPhoto::class], version = 1)
@TypeConverters(Converters::class)
abstract class EverPicsDatabase : RoomDatabase() {
    abstract fun unsplashPhotoDao(): UnsplashPhotoDao
}