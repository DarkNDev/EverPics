package com.darkndev.everpics.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "unsplash_photo")
@Parcelize
data class UnsplashPhoto(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val created_at: String,
    val description: String?,
    val height: Int,
    val likes: Int,
    val links: Links,
    val urls: Urls,
    val user: User,
    val width: Int
) : Parcelable {
    @Parcelize
    data class Links(
        val download: String,
        val html: String
    ) : Parcelable

    @Parcelize
    data class Urls(
        val full: String,
        val raw: String,
        val regular: String,
        val small: String
    ) : Parcelable

    @Parcelize
    data class User(
        val id: String,
        val instagram_username: String?,
        val name: String,
        val portfolio_url: String?,
        val profile_image: ProfileImage,
        val twitter_username: String?,
        val username: String
    ) : Parcelable {
        val userUrl get() = "https://unsplash.com/$username?utm_source=EverPics&utm_medium=referral"

        @Parcelize
        data class ProfileImage(
            val large: String,
            val medium: String,
            val small: String
        ) : Parcelable
    }
}