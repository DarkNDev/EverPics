package com.darkndev.everpics.di

import android.app.Application
import androidx.room.Room
import com.darkndev.everpics.api.UnsplashApi
import com.darkndev.everpics.api.UnsplashApi.Companion.BASE_URL
import com.darkndev.everpics.database.EverPicsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EverPicsModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ) = Room.databaseBuilder(app, EverPicsDatabase::class.java, "everPics_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideUnsplashDao(database: EverPicsDatabase) = database.unsplashPhotoDao()
}