package com.darkndev.everpics.database

import androidx.room.TypeConverter
import com.darkndev.everpics.models.UnsplashPhoto
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toLinks(linksString: String): UnsplashPhoto.Links {
        return Gson().fromJson(linksString, UnsplashPhoto.Links::class.java)
    }

    @TypeConverter
    fun fromLinks(links: UnsplashPhoto.Links): String {
        return Gson().toJson(links)
    }

    @TypeConverter
    fun toUrls(urlsString: String): UnsplashPhoto.Urls {
        return Gson().fromJson(urlsString, UnsplashPhoto.Urls::class.java)
    }

    @TypeConverter
    fun fromUrls(links: UnsplashPhoto.Urls): String {
        return Gson().toJson(links)
    }

    @TypeConverter
    fun toUser(userString: String): UnsplashPhoto.User {
        return Gson().fromJson(userString, UnsplashPhoto.User::class.java)
    }

    @TypeConverter
    fun fromUser(user: UnsplashPhoto.User): String {
        return Gson().toJson(user)
    }
}