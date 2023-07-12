package com.darkndev.everpics.ui.details

import android.Manifest
import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.darkndev.everpics.database.UnsplashPhotoDao
import com.darkndev.everpics.features.ImageDownloader
import com.darkndev.everpics.models.UnsplashPhoto
import com.darkndev.everpics.utils.checkPermission
import com.darkndev.everpics.utils.sdkVersion29AndAbove
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val unsplashPhotoDao: UnsplashPhotoDao,
    state: SavedStateHandle,
    private val application: Application
) : ViewModel() {

    val statePhoto = state.get<UnsplashPhoto>("photo")!!
    val databasePhoto = unsplashPhotoDao.getPhoto(statePhoto.id).asLiveData()
    var writePermissionGranted = false

    fun bookmarkClicked() = viewModelScope.launch {
        val databasePhoto = databasePhoto.value
        if (databasePhoto != null && databasePhoto == statePhoto) {
            unsplashPhotoDao.deletePhotos(databasePhoto)
        } else {
            unsplashPhotoDao.upsertPhotos(statePhoto)
        }
    }

    fun checkWritePermissionAndDownload(url: String) = viewModelScope.launch(Dispatchers.IO) {
        writePermissionGranted = sdkVersion29AndAbove {
            true
        } ?: checkPermission(
            application,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (writePermissionGranted) {
            val downloader = ImageDownloader(application)
            downloader.downloadFile(url)
        } else {
            channel.send(Event.CheckWritePermission)
        }
    }

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    sealed class Event {
        object CheckWritePermission : Event()
    }

}