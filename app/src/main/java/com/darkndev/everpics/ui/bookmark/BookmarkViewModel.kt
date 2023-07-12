package com.darkndev.everpics.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darkndev.everpics.database.UnsplashPhotoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    unsplashPhotoDao: UnsplashPhotoDao
) : ViewModel() {

    val photosList = unsplashPhotoDao.getPhotos().asLiveData()
}